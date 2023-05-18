import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Program {
    private final File WHITELIST = new File("src/whiteList.txt");
    private final File ARTISTSWHITELIST = new File("src/artistsWhiteList.txt");
    private ArrayList<Path> whiteList;
    private DataModifyer dataModifyer;

    public Program() {
        dataModifyer = new DataModifyer();
        whiteList = new ArrayList<>();
        importWhiteList();
    }


    public void runRenameFileTree(Path path) {
        try {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return checkWhiteList(dir) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    dataModifyer.changeMetadata(file.toFile());
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
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateGenreInFolder(Path path, String genre) {
        try {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return checkWhiteList(dir) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    dataModifyer.changeGenre(file.toFile(), genre);
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
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void autoMooveFileToFolder(Path musicPath, Path dirToSort) {
        ArrayList<String> directories = getDirectories(musicPath);
        try {
            Files.walkFileTree(dirToSort, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return (dir.equals(musicPath)) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String artist = dataModifyer.getArtist(file.toFile()).toLowerCase();
                    System.out.println("Trying to moove: " + artist);
                    for (int i = 0; i < directories.size(); i++) {
                        if(directories.get(i).equals(artist)){
                            Files.move(file, findDir(musicPath, directories.get(i)).resolve(file.getFileName()));
                        }
                    }
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
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

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

    private ArrayList<String> getDirectories(Path startPath) {
        ArrayList<String> temp = new ArrayList<>();
        try {
            Files.walkFileTree(startPath, new FileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            if (checkWhiteList(dir)) {
                                return FileVisitResult.SKIP_SUBTREE;
                            }
                            temp.add(dir.getFileName().toString().toLowerCase());
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
                    }
            );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return temp;
    }

    private Path findDir(Path startPath, String dirToCheck) {
        final Path[] result = {null};

        try {
            Files.walkFileTree(startPath, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if(checkWhiteList(dir)) return FileVisitResult.SKIP_SUBTREE;
                    if(dir.getFileName().toString().toLowerCase().equals(dirToCheck)) result[0] = dir;
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
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result[0];
    }
}
