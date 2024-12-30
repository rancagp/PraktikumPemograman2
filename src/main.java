import model.mybatisutil;
import model.usermapper;
import model.user; // Pastikan user diimpor
import org.apache.ibatis.session.SqlSession;

import view.userPdf;
import view.userview;
import controller.usercontroller;

import java.util.stream.IntStream;

public class main {
    public static void main(String[] args) {
        // Inisialisasi sesi MyBatis
        SqlSession session = mybatisutil.getSqlSession();
        usermapper mapper = session.getMapper(usermapper.class);
        userPdf pdf = new userPdf();

        // Simulasi data besar
        IntStream.range(1, 101).forEach(i -> {
            user newUser = new user();
            newUser.setName("User " + i);
            newUser.setEmail("user" + i + "@example.com");
            mapper.insertUser(newUser);
        });
        session.commit(); // Jangan lupa commit transaksi untuk menyimpan data

        // Inisialisasi tampilan dan controller
        userview view = new userview();
        new usercontroller(view, mapper, pdf);

        // Menampilkan tampilan
        view.setVisible(true);
    }
}
