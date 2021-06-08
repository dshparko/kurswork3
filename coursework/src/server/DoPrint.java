package server;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoPrint extends Thread {

    String file_name;
    FileInputStream fileInputStream;

    public DoPrint(String path) {
        file_name = path;
    }

    @Override
    public void run() {
        try {
            fileInputStream = new FileInputStream(file_name);

            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();//Находит службу печати по умолчанию для этой среды. Это может вернуть null. Если каждая из нескольких служб поиска задает значение по умолчанию, выбранная служба не определена точно, но по умолчанию обычно возвращается собственная служба платформы, а не установленная служба. Если нет четко идентифицируемой встроенной службы печати по умолчанию для платформы, по умолчанию используется первая служба, которая будет расположена в зависимости от реализации.
            StartServer._ta.append("Идёт печать на принтер " + printService.getName() + "\n");

            DocPrintJob job = printService.createPrintJob();//Создает и возвращает PrintJob, способное обрабатывать данные из любого из поддерживаемых типов документов.

            DocFlavor[] docFlavors = printService.getSupportedDocFlavors();//Определяет форматы данных печати, которые клиент может указать при настройке для этого задания PrintService.
            for (int i = 0; i < docFlavors.length; i++) {
                System.out.println(docFlavors[i].toString());
            }
            DocFlavor docFlavor = getFlavorFromFilename(file_name);
            StartServer._ta.append("Используется формат " + docFlavor.toString() + "\n");

            DocAttributeSet docAttributes = new HashDocAttributeSet();//Созд новый пустой набор атрибутов хэш-документа.
            docAttributes.add(OrientationRequested.PORTRAIT);//ориентация
            PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
            printAttributes.add(new Copies(1));
            printAttributes.add(new JobName("printed", null));

            Doc doc = new SimpleDoc(fileInputStream, docFlavor, docAttributes);//Создает SimpleDocс указанными данными печати, типом документа и набором атрибутов документа.
            try {
                job.print(doc, printAttributes);
            } catch (PrintException pe) {
                StartServer._ta.append("Печать прервана: " + pe.getLocalizedMessage() + "\n");
                System.out.println(pe.getLocalizedMessage());
                return;
            }
            StartServer._ta.append("Печать успешно завершена!\n");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DoPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DocFlavor getFlavorFromFilename(String file_name) {
        String extension = file_name.substring(file_name.lastIndexOf('.') + 1);
        extension = extension.toLowerCase();
        switch (extension) {
            case "gif":
                return DocFlavor.INPUT_STREAM.GIF;
            case "jpeg":
            case "jpg":
                return DocFlavor.INPUT_STREAM.JPEG;
            case "png":
                return DocFlavor.INPUT_STREAM.PNG;
            case "ps":
                return DocFlavor.INPUT_STREAM.POSTSCRIPT;
            case "txt":
                return DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;
            case "pdf":
                return DocFlavor.INPUT_STREAM.PDF;
            // Fallback: try to determine flavor from file content
            default:
                return DocFlavor.INPUT_STREAM.AUTOSENSE;
        }
    }


}
