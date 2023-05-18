import java.nio.file.Path;

public class Main {

    private static final Path MUSICFOLDER = Path.of("C:\\Users\\BAT2\\Desktop\\pen\\musica");
    private static final Path DEFAULTDIRTOSORT = Path.of("C:\\Users\\BAT2\\Desktop\\pen\\musica");

    public static void main(String[] args) {
        //new Program().updateGenreInFolder(Path.of("C:\\Users\\BAT2\\Desktop\\pen\\musica\\extremoduro"), "Rock");
        new Program().runRenameFileTree(MUSICFOLDER);
    }
}