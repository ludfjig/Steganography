/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steganography;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Ludvig
 */
public class MyCustomFilter extends FileFilter{
      @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".txt" extension
            return true; //file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
        }
        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return "Text documents (*.txt)";
        }
}
