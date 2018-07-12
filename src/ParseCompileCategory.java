import bean.GradleFile;
import util.FileUtil;
import util.L;
import util.ParseUtil;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ParseCompileCategory {
    public static void main(String args[]) {

        String baseProjectPath = "D:\\starProject\\star1";
        File baseFile = new File(baseProjectPath);
        File[] files = baseFile.listFiles();
        L.l("==============start================");
        int index = 0;
        for (File file : files) {
            //只遍历文件
            if (file.isDirectory()) {
                //读取到文件
                List<GradleFile> gradleFileList = FileUtil.getAllGradleFiles(file.getAbsolutePath());
                L.l("当前读取第", String.valueOf(index), "个文件", "目录路径为", file.getAbsolutePath());
                index++;
                if (gradleFileList == null || gradleFileList.size() == 0 || gradleFileList.size() == 1) {
                    L.l("未找到gradle文件");
                    continue;
                }
                int size = gradleFileList.size();
                L.l("共找到",String.valueOf(size),"个文件");
                for (int i = 1; i < size; i++) {
                    GradleFile gradleFile = gradleFileList.get(i);
                    ParseUtil.parse(gradleFile.getContent(), gradleFile.getPath());
                }
            }
        }
        Set<String> set = ParseUtil.getMethodSet();
        for (String s : set) {
            System.out.println(s);
        }
    }

}
