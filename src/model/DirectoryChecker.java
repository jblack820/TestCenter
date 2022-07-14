package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DirectoryChecker extends SimpleFileVisitor<Path> {

    public static final List<String> OPENED_FILES = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (isFileOpened(file)) {
            OPENED_FILES.add(file.getFileName().toString());
        }
        return FileVisitResult.CONTINUE;
    }

    private boolean isFileOpened(Path file) {
        File originalFile = file.toFile();
        String origilaFileName = originalFile.getAbsolutePath();
        File sameFileName = new File(origilaFileName);
        return !originalFile.renameTo(sameFileName);
    }
}
