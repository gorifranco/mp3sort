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
    public MusicMetadataSet getSongData(File file) {
        MusicMetadataSet set = null;
        try {
            set = new MyID3().read(file);
        } catch (IOException | ID3ReadException e) {
            e.printStackTrace();
        }
        return set;
    }

    public String  getArtist(File file){
        MusicMetadataSet set = getSongData(file);
        IMusicMetadata metadata = set.getSimplified();
        return metadata.getArtist();
    }

    public void changeGenre(File file, String genre) {
        //Hip-Hop
        //Metal
        //Pop
        //Rap
        //Techno
        //Ska
        //Ambient
        //...  ID3v1Genre.java
        MusicMetadataSet set = getSongData(file);
        IMusicMetadata metadata = set.getSimplified();
        MusicMetadata meta = new MusicMetadata("newset");
        meta.setAlbum(metadata.getAlbum());
        meta.setArtist(metadata.getArtist());
        meta.setSongTitle(metadata.getSongTitle());
        meta.setGenreName(genre);
        meta.setYear(metadata.getYear());
        meta.setTrackNumberNumeric(metadata.getTrackNumberNumeric());
        try {
            new MyID3().update(file, set, meta);
        } catch (UnsupportedEncodingException e1) {
            System.out.println(e1.getMessage());
        } catch (IOException e2) {
            System.out.println(e2.getMessage());
        } catch (ID3WriteException e3) {
            System.out.println(e3.getMessage());
        }
    }

    public void changeMetadata(File file) {
        MusicMetadataSet set = getSongData(file);
        String[] artistTitle = null;

        boolean renamed = false;
        String artist = "";
        String album = "";
        String song_title = "";
        String genreName = "";
        Number year = null;
        Number trackNumber = null;
        Number genreID = null;

        if (set == null) // perhaps no metadata
        {
        } else {
            try {
                IMusicMetadata metadata = set.getSimplified();
                artist = metadata.getArtist();
                album = metadata.getAlbum();
                song_title = metadata.getSongTitle();
                genreID = metadata.getGenreID();
                year = metadata.getYear();
                genreName = metadata.getGenreName();
                trackNumber = metadata.getTrackNumberNumeric();
                if (artist == null || song_title == null) {
                    artistTitle = getArtistTitle(file.getName());
                    if (artistTitle != null) {
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
            meta.setGenre(genreName, genreID);
            meta.setYear(year);
            meta.setTrackNumberNumeric(trackNumber);

            try {
                new MyID3().update(file, set, meta);
                if (renamed) file.renameTo(new File(Path.of(file.getPath()).getParent() + "\\" + artistTitle[1]));

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
        song = llevarBarrabaxes(song);
        if (countChars(song, '-') == 1){
            String[] aux = song.split("-");
            if(aux[1].charAt(0) == ' ') aux[1] = aux[1].substring(1, aux[1].length()-1);
            if(aux[0].charAt(aux[0].length()-1) == ' ') aux[0] = aux[0].substring(0, aux[0].length()-2);
            return aux;
        }

        System.out.println("cant rename: " + song);
        return null;
    }

    private String quitarExtension(String file) {
        System.out.println(file);
        if (file.contains(".")) {
            String[] temp = file.split("\\.");
            System.out.println(temp[temp.length - 1]);
            return file.substring(0, file.length() - temp[temp.length - 1].length() - 1);
        }
        return file;
    }
    private String llevarBarrabaxes(String cadena){
return cadena.replace('_', ' ');
    }
}