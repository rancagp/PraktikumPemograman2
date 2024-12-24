import model.mybatisutil;
import model.usermapper;
import org.apache.ibatis.session.SqlSession;
import view.userview;
import controller.usercontroller;

public class main {
    public static void main(String[] args) {
        SqlSession session = mybatisutil.getSqlSession();
        usermapper mapper = session.getMapper(usermapper.class);

        userview view = new userview();
        new usercontroller(view, mapper);

        view.setVisible(true);
    }
}