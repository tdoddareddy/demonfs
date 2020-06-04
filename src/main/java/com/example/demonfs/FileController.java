package com.example.demonfs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.cfenv.core.CfEnv;
import io.pivotal.cfenv.core.CfService;

@RestController
public class FileController {

    Logger logger = LoggerFactory.getLogger("FileController.class");

    @RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
    }
    
    private Path getDataPath(){
        Path dataPath = FileSystems.getDefault().getPath("/Users/tdoddareddy/tests");
        try{
            CfEnv cfEnv = new CfEnv();
            CfService nfsService = cfEnv.findServiceByLabel("nfs");
            dataPath = nfsService.createVolumes().get(0).getPath();
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }
        return dataPath;
    }

    @RequestMapping("/file/list")
    public Set<String> listFilesUsingDirectoryStream() throws IOException {

        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(getDataPath())) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.getFileName()
                        .toString());
                }
            }
        }
        return fileList;
    }

    @RequestMapping(value = "/file/create/{fileName}", method = RequestMethod.PUT)
    @ResponseBody
    public String createFile(@PathVariable("fileName") String fileName, @RequestBody String fileBody){
        Path filePath = Paths.get(getDataPath().toString() + "/" + fileName);
        try{

            List<String> lines = new ArrayList<String>();
            lines.add(fileBody);
            Path file = Files.createFile(filePath);
            Files.write(file, lines, StandardCharsets.UTF_8);
        }catch(IOException ioex){
            logger.error(ioex.getMessage());
        }
        return "File created at " + filePath.toString();

    }

    @RequestMapping(value = "/file/delete/{fileName}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteFile(@PathVariable("fileName") String fileName){

        boolean deleted = false;
        Path filePath = Paths.get(getDataPath().toString() + "/" + fileName);
        StringBuilder builder = new StringBuilder();
        try{
            deleted = Files.deleteIfExists(filePath);
        }catch(IOException ioex){
            logger.error(ioex.getMessage());
        }
        builder.append("File deleted at ").append(filePath.toString()).append(" : ").append(deleted);
        return builder.toString();

    }
    
}