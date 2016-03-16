import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.sun.javafx.collections.MappingChange.Map;


public class WordCount {

	@Test
	public void count() throws IOException, InterruptedException{
		File file=new File("E:\\OES_EndSide");
		HashMap<String, Integer> wordMap=new HashMap();
		for (File file2 : traverseFolder2(file.getAbsolutePath())) {
			String fileString=FileUtils.readFileToString(file2);
			fileString.replaceAll("\\s+", " ");
			fileString.replaceAll("\\n+"," ");
			fileString.replaceAll("[^a-zA-z]", "");
			fileString.trim();
			StringUtils.deleteWhitespace(fileString);
			String[] strings=fileString.split(" ");
			for (int i = 0; i < strings.length; i++) {
				
				if (!strings[i].equals(" ")&&!strings[i].equals("\\t")&&!strings[i].equals("\\n")) {
					System.out.println(strings[i]);
				}
			}
		}
	}
	
	public ArrayList<File> traverseFolder2(String path) {
		ArrayList<File> fileList=new ArrayList<File>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return fileList;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        fileList.addAll(traverseFolder2(file2.getAbsolutePath()));
                    } else {
                    	if (file2.getName().endsWith(".java")) {
                    		fileList.add(file2);
						}
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return fileList;
    }
	
}
