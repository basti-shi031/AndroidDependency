package util;

import bean.ProjectInfo;

import java.sql.*;

public class DBUtil {
    static Connection conn;
    static Statement stmt;

    public static void init() {
        // 数据库的用户名与密码，需要根据自己的设置
        final String USER = "root";
        final String PASS = "root";
        final String DB_URL = "jdbc:mysql://10.141.221.73:3306/codehub?useSSL=false";
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
    }

    public static ProjectInfo getProjectInfo(String like) {
        try {
            stmt = conn.createStatement();

            String sql;
            sql = "SELECT * FROM repository_high_quality where repos_addr like  '%" + like + "'";
            L.l(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ProjectInfo projectInfo;
            while (rs.next()) {
                if (rs.getLong("stars") >= 1000) {
                    long stars = rs.getLong("stars");
                    long id = rs.getLong("id");
                    long repository_id = rs.getLong("repository_id");
                    String url = rs.getString("url");
                    long commit_count = rs.getLong("commit_count");
                    long sizes = rs.getLong("sizes");
                    long fork = rs.getLong("fork");
                    String repos_addr = rs.getString("repos_addr");
                    projectInfo = new ProjectInfo(commit_count, fork, sizes, repos_addr, repository_id, id, stars, url);
                    return projectInfo;
                } else {
                    return null;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
