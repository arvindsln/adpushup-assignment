package com.project.adpushup.reader;

import static com.project.adpushup.constant.Constants.DIR_LOC;
import static com.project.adpushup.constant.Constants.POOL_SIZE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.project.adpushup.processor.DataFileProcessor;

@Component
public class DataFileReader {

    public void readFiles() throws IOException {


        List<File> files = Files.list(Paths.get(DIR_LOC))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".csv"))
                .map(Path::toFile)
                .collect(Collectors.toList());


        processFiles(files);
    }

    private void processFiles(List<File> files) throws FileNotFoundException {

        ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);

        for (File file : files) {
            String[] pathStr = file.getPath().split("\\\\");
            String fileName = pathStr[2].replace(".csv", "");
            Scanner scanner = new Scanner(file);

            es.execute(new DataFileProcessor(fileName, scanner));
        }
        es.shutdown();
    }
}
