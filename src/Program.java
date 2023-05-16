import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Program {
    private String carpetesDiscografies;
    private Path carpetaMusica;

    public Program(String carpetesDiscografies, Path carpetaMusica) {
        this.carpetesDiscografies = carpetesDiscografies;
        this.carpetaMusica = carpetaMusica;
    }

    public Program(Path carpetaMusica) {
        this.carpetaMusica = carpetaMusica;
        runRenameFileTree();
    }

    public void runRenameFileTree() {
        try {
            Files.walkFileTree(carpetaMusica, new RenameAndMetaFVfiletree(true));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
