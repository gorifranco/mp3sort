package org.cmc.music.myid3.id3v2;

import org.cmc.music.common.ID3FrameType;
import org.cmc.music.metadata.ImageData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface IFrameFactory {
    public ID3v2OutputFrame createCommentFrame(String value)
            throws UnsupportedEncodingException, IOException;

    public ID3v2OutputFrame createUserTextFrame(String value1, String value2)
            throws UnsupportedEncodingException, IOException;

    public ID3v2OutputFrame createTextFrame(ID3FrameType frameType, String value)
            throws UnsupportedEncodingException, IOException;

    public ID3v2OutputFrame createTextFrame(ID3FrameType frameType,
                                            String value1, String value2) throws UnsupportedEncodingException,
            IOException;

    public ID3v2OutputFrame createPictureFrame(ImageData imageData)
            throws UnsupportedEncodingException, IOException;

}
