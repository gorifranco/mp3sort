import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class RenameAndMetaFVfiletree implements FileVisitor<Path> {
    private ArrayList<Path> whiteList;
    private final boolean walkTree;

    public RenameAndMetaFVfiletree(boolean walkTree) {
        this.whiteList = new ArrayList<>();
        this.walkTree = walkTree;
        importWhiteList();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if(walkTree) return checkWhiteList(dir) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        new DataModifyer(new File(file.toUri())).changeMetadata();
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

    //WHITE LIST
    private boolean checkWhiteList(Path path) {
        return whiteList.contains(path);
    }

    private void importWhiteList() {
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/whiteList"));
            line = br.readLine();
            while (line != null) {
                whiteList.add(Path.of(line));
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("White list not found");
        } catch (IOException e) {
            System.out.println("IO exception");
        }
    }
}