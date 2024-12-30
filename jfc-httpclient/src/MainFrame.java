package jfc.httpclient;

import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.util.Timeout;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame {

    public static void main(String[] args) {
        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(5))
                .build();

        final CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .build();

        client.start();

        final HttpHost target = new HttpHost("672fbf9066e42ceaf15e9a9b.mockapi.io");
        final String requestUri = "/api/contacts";

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Contoh HTTP Client di Swing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new BorderLayout());

            JLabel statusLabel = new JLabel("Tekan Tombol untuk mulai mengunduh data", JLabel.CENTER);
            JButton startButton = new JButton("Mulai");
            JProgressBar progressBar = new JProgressBar(0, 100);
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(startButton);
            buttonPanel.add(progressBar);

            frame.add(statusLabel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    client.close(CloseMode.GRACEFUL);
                    System.exit(0);
                }
            });

            final SimpleHttpRequest request = SimpleRequestBuilder.get()
                    .setHttpHost(target)
                    .setPath(requestUri)
                    .build();

            startButton.addActionListener(e -> {
                progressBar.setIndeterminate(true);
                startButton.setEnabled(false);
                statusLabel.setText("Proses Berjalan");
                textArea.setText("");

                client.execute(
                        SimpleRequestProducer.create(request),
                        SimpleResponseConsumer.create(),
                        new FutureCallback<SimpleHttpResponse>() {
                            @Override
                            public void completed(SimpleHttpResponse result) {
                                System.out.println(request + "->" + new StatusLine(result));
                                System.out.println(result.getBodyText());

                                JSONParser parser = new JSONParser();
                                try {
                                    JSONArray contacts = (JSONArray) parser.parse(result.getBodyText());
                                    contacts.forEach(obj -> {
                                        JSONObject contact = (JSONObject) obj;
                                        String line = "Name: " + contact.get("name") + ", Phone: " + contact.get("phone");
                                        textArea.append(line + "\n");
                                    });
                                } catch (ParseException ex) {
                                    throw new RuntimeException(ex);
                                }

                                progressBar.setIndeterminate(false);
                                statusLabel.setText("Proses Selesai");
                            }

                            @Override
                            public void failed(Exception ex) {
                                ex.printStackTrace();
                                progressBar.setIndeterminate(false);
                                statusLabel.setText("Proses Gagal");
                            }

                            @Override
                            public void cancelled() {
                                progressBar.setIndeterminate(false);
                                statusLabel.setText("Proses Dibatalkan");
                            }
                        }
                );
            });

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
