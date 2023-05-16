import org.cmc.music.common.ID3ReadException;
import org.cmc.music.common.ID3WriteException;
import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class Program {

    public void run(){
        Path actualPath = FileSystems.getDefault().getPath("a");
        String[] archives = getRegularArchives(String.valueOf(actualPath));
        for (int i = 0; i < archives.length; i++) {
            String secondDir = getSecondDir(archives[i]);
            if (Files.isDirectory(Path.of(secondDir))){
                changeGenere(new File(archives[i]), secondDir);
            }
        }
    }

    public MusicMetadataSet getSongData(File file) {
        MusicMetadataSet set = null;
        try {
            set = new MyID3().read(file);
        } catch (IOException | ID3ReadException e) {
            e.printStackTrace();
        }
        return set;
    }



    public String getFirstDir(){
        String actualDir = "";
        try {
            actualDir = new File(".").getCanonicalPath();
        }catch (IOException e){
            e.printStackTrace();
        }
    return actualDir;
    }


    public String getSecondDir(String path){
        String initialPath = getFirstDir();
        String secondDir = "";
        boolean found = false;
        int i = 0;
        String[] pathSplited = path.split("/");
        while(!found){
            if(pathSplited[i].equals(initialPath)){
                secondDir = pathSplited[i+1];
                found = true;
            } else {
                i++;
            }
        }
        return secondDir;
    }


    public String[] getRegularArchives (String path){

        ArrayList<String> list = new ArrayList<>();
        try {
            String[] filesPaths = Arrays.toString(Files.walk(Path.of(path)).filter(Files::isRegularFile).toArray()).split(",");
            list.addAll(Arrays.asList(filesPaths));
        }catch (IOException e){
            e.printStackTrace();
        }
        String[] result = new String[list.size()];
        result = list.toArray(result);

        return result;
    }

    public void changeGenere(File file, String genereName) {
        MusicMetadataSet set = getSongData(file);
        if (set == null) // perhaps no metadata
        {
            System.out.println("no metadata");
        } else {
            System.out.println("metadata found");
            String artist = "";
            String album = "";
            String song_title = "";

            Number year = null;
            try {
                IMusicMetadata metadata = set.getSimplified();
                artist = metadata.getArtist();
                album = metadata.getAlbum();
                song_title = metadata.getSongTitle();

                year = metadata.getYear();

            } catch (Exception e) {
                e.printStackTrace();
            }
            MusicMetadata meta = new MusicMetadata("newset");
            meta.setAlbum(album);
            meta.setArtist(artist);
            meta.setSongTitle(song_title);
            meta.setGenreName(genereName);
            meta.setYear(year);
            try {
                new MyID3().update(file, set, meta);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ID3WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  // write updated metadata
        }
    }
}
