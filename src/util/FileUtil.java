package util;


import bean.GradleFile;
import config.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 根据目录名判断，该目录下是否可能有build.gradle
     *
     * @param file
     * @return
     */
    private static boolean isPossibleDirectory(File file) {
        return !Config.PASS_FILE.contains(file.getName());
    }

    /**
     * 读文件
     *
     * @param path
     * @return
     */
    public static String read(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));

            String tempString = "";
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                result.append(tempString).append("\r\n");
                line++;
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    return result.toString();
                } catch (IOException e1) {
                }
            }
        }

        return null;
    }

    /**
     * 找到项目中的Gradle文件，存入列表，该列表第0项为gradle.properties，该文件可能会存储三方库的版本号
     *
     * @param projectPath 项目地址
     * @return gradleFiles item为gradleFile的list
     */
    private static List<GradleFile> findGradleFilePath(String projectPath) {
        List<GradleFile> gradleFiles = new ArrayList<>();
        //遍历文件夹，找到所有的build.gradle文件
        traverseFile(projectPath, gradleFiles);
        return gradleFiles;
    }

    /**
     * 遍历文件夹
     *
     * @param projectPath
     * @param gradleFiles
     */
    private static void traverseFile(String projectPath, List<GradleFile> gradleFiles) {
        File baseFile = new File(projectPath);
        if (baseFile.exists()) {
            //文件存在
            File[] files = baseFile.listFiles();
            if (files.length == 0) {
                return;
            } else {
                for (File file : files) {
                    //如果是文件，直接处理
                    if (file.isFile()) {
                      /*  if (file.getName().equals("gradle.properties")) {
                           // L.l(file.getPath());
                            GradleFile gradleFile = new GradleFile(file.getPath());
                            gradleFile.setType("gradle.properties");
                            gradleFiles.add(gradleFile);
                        } else*/
                        if (file.getName().endsWith(".gradle")) {
                            if (file.getName().equals("build.gradle")) {
                                //是项目构建需要的build.gradle
                                GradleFile gradleFile = new GradleFile(file.getPath());
                                gradleFile.setType("build.gradle");
                                gradleFile.setBuildFile(true);
                                gradleFile.setRootFile(isRootFile(file.getAbsolutePath()));
                                gradleFiles.add(gradleFile);
                            } else {
                                //不是项目构建的gradle，是开发者自己写的gradle
                                GradleFile gradleFile = new GradleFile(file.getPath());
                                gradleFile.setBuildFile(false);
                                gradleFile.setRootFile(isRootFile(file.getAbsolutePath()));
                                gradleFiles.add(gradleFile);
                            }
                        }
                    } else if (file.isDirectory() && isPossibleDirectory(file)) {
                        //如果是目录，需要递归且满足条件
                        traverseFile(file.getAbsolutePath(), gradleFiles);
                    }
                }
            }
        } else {
            System.out.println("文件不存在");
        }
    }

    /**
     * 读取文件
     *
     * @param files
     */
    private static List<GradleFile> readBuildContent(List<GradleFile> files) {
        if (files == null || files.size() == 0 || files.size() == 1) {
            return null;
        }
        for (GradleFile file : files) {
            file.setContent(FileUtil.read(file.getPath()));
        }
        return files;
    }

    public static List<GradleFile> getAllGradleFiles(String projectPath) {
        return readBuildContent(findGradleFilePath(projectPath));
    }

    /**
     * 判断该文件是否位于项目根目录，由路径的最后一部分中是否包含__fdse__来判断
     *
     * @param path
     * @return
     */
    private static boolean isRootFile(String path) {
        String[] path_part = path.split("/");
        int size = path_part.length;
        if (size == 0) {
            return false;
        }
        return path_part[size - 1].contains("__fdse__");

    }

}
