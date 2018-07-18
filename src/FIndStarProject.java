import com.google.gson.Gson;
import util.L;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FIndStarProject {
    public static void main(String args[]) {
        final String DB_URL = "jdbc:mysql://10.141.221.73:3306/codehub?useSSL=false";

        // 数据库的用户名与密码，需要根据自己的设置
        final String USER = "root";
        final String PASS = "root";
/*        String baseStarProjectPath = args[0];
        final int MAX = 83;
        List<File> projectFiles = new ArrayList<>();
        for (int i = 1; i < MAX; i++) {
            String projectPath = baseStarProjectPath + "//starproject" + String.valueOf(i);
            File[] tempFiles = new File(projectPath).listFiles();
            //筛去非dir类型的文件
            for (File projectFile : tempFiles) {
                if (projectFile.isDirectory()) {
                    projectFiles.add(projectFile);
                }
            }
        }
        List<String> projectName = new ArrayList<>();
        int size = projectFiles.size();
        L.l(String.valueOf(size));*/
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<String> projectName = new ArrayList<>();
  /*      for (int i = 0; i < size; i++) {
            File file = projectFiles.get(i);
            L.l(file.getName());
            String[] name = file.getName().split("__fdse__");
            if (name.length < 2) {
                L.l("========", file.getAbsolutePath());
                continue;
            }
            String company = name[0];
            String project = name[1];*/

        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM repository_high_quality where stars>=500";
            L.l(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String url = rs.getString("url");
                String[] urls = url.split("/");
                int size = urls.length;
                String company = urls[size - 2];
                String project = urls[size - 1];

                String path = "/home/fdse/data/prior_repository/" + company + "/" + project;
                projectName.add(path);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // }
        L.l(String.valueOf(projectName.size()));
        String json = new Gson().toJson(projectName);
        L.l(json);

        // 完成后关闭

        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
