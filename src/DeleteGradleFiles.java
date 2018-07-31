import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DeleteGradleFiles {
    public static void main(String[] args) {
        String basePath = "D:\\cs\\projects_last\\gradle500\\";
        File[] projects = new File(basePath).listFiles();
        for (File project : projects) {
            System.out.println(project.getAbsolutePath());
            deleteFile(project);
        }

    }

    private static void deleteFile(File project) {
        Queue<File> q = new LinkedList<>();
        List<File> propertiesFiles = new ArrayList<>();
        q.offer(project);
        while (!q.isEmpty()) {
            File f = q.poll();
            if (f.isFile() && f.getName().endsWith(".properties")&&f.getName().contains("_2")) {
                propertiesFiles.add(f);
            } else if (f.isDirectory()) {
                File[] temp = f.listFiles();
                for (File file : temp) {
                    q.offer(file);
                }
            }
            for (File deleteFile : propertiesFiles) {
                deleteFile.delete();
            }
        }
    }
}
