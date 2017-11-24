package com.argusoft.hkg.web.center.util;

import com.argusoft.hkg.common.functionutil.FolderManagement;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author rajkumar
 */
@Service
public class BarcodeUtil {

    public static final Logger log = LoggerFactory.getLogger(BarcodeUtil.class);

    public String generateBarcode(String payload) throws IOException {
        String tempFileName = payload;
        Code128Bean code128Bean = new Code128Bean();
        //Output stream to which store barcode data
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        code128Bean.doQuietZone(true);
        try {
            // Set up the canvas provider for monochrome JPEG output
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out,
                    "image/jpeg", 350, BufferedImage.TYPE_BYTE_BINARY,
                    false, 0);
            // Generate the barcode
            code128Bean.generateBarcode(canvas, payload);
            // Signal end of generation
            canvas.finish();
            FolderManagement.storeFileInTemp(tempFileName, out.toByteArray(), false);

        } catch (IOException e) {
            log.error(e.toString());
        } finally {
            out.close();
        }
        return tempFileName;
    }
}
