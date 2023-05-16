import org.cmc.music.common.ID3ReadException;
import org.cmc.music.common.ID3WriteException;
import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

public class DataModifyer {
    File file;

    public DataModifyer(File file) {
        this.file = file;
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

    public void changeMetadata() {
        MusicMetadataSet set = getSongData(file);
        String[] artistTitle = null;

        boolean renamed = false;
        String artist = "";
        String album = "";
        String song_title = "";
        String genreName = "";
        Number year = null;
        Number trackNumber = null;

        if (set == null) // perhaps no metadata
        {
        } else {
            try {
                IMusicMetadata metadata = set.getSimplified();
                artist = metadata.getArtist();
                album = metadata.getAlbum();
                song_title = metadata.getSongTitle();
                year = metadata.getYear();
                genreName = metadata.getGenreName();
                trackNumber = metadata.getTrackNumberNumeric();
                if(artist == null || song_title == null){
                    artistTitle = getArtistTitle(file.getName());
                    if(artistTitle != null){
                        artist = artistTitle[0];
                        song_title = quitarExtension(artistTitle[1]);
                        renamed = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MusicMetadata meta = new MusicMetadata("newset");
            meta.setAlbum(album);
            meta.setArtist(artist);
            meta.setSongTitle(song_title);
//            meta.setGenreName(genreName);
            meta.setYear(year);
            meta.setTrackNumberNumeric(trackNumber);

            try {
                new MyID3().update(file, set, meta);
                if(renamed) file.renameTo(new File(Path.of(file.getPath()).getParent()  + "\\" + artistTitle[1]));

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
    private int countChars(String string, char c) {
        int contador = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c) contador++;
        }
        return contador;
    }

    private String[] getArtistTitle(String song) {
        if (countChars(song, '-') == 1) return song.split("-");
        System.out.println("cant rename: " + song);
        return null;
    }
    private String quitarExtension(String file){
        System.out.println(file);
        if(file.contains(".")){
            String[] temp = file.split("\\.");
            System.out.println(temp[temp.length-1]);
            return file.substring(0, file.length() - temp[temp.length-1].length()-1);
        }
        return file;
    }
}