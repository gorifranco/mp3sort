import javax.imageio.stream.FileImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class CustomFileVisitor implements FileVisitor<Path> {
    private ArrayList<String> whiteList;

    public CustomFileVisitor() {
        this.whiteList = new ArrayList<>();
        importWhiteList();

    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    private void checkWhiteList(String path){

    }
    private void importWhiteList() throws IOException {
        FileImageInputStream fis = new FileImageInputStream(new File("whiteList.txt"));
    }
}
