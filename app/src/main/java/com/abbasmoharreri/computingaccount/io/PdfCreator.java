package com.abbasmoharreri.computingaccount.io;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.FontFamily;
import android.os.Environment;

import com.abbasmoharreri.computingaccount.MainActivity;
import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AReport;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.static_value.PdfLanguage;
import com.abbasmoharreri.computingaccount.text.NumberTextWatcherForThousand;
import com.abbasmoharreri.computingaccount.text.TextProcessing;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.itextpdf.text.Font.BOLD;

public class PdfCreator {

    private Context context;
    private String[] headerEnglish = {"Number", "Date", "Description", "Price", "Comment"};
    private String[] headerFarsi = {"توضیحات", "هزینه", "شرح", "تاریخ", "ردیف"};
    private String[] titleEnglish = {"Salary Report", "Person Name: ", "Report Number: ", "Start Date: ", "End Date: ", "Report Date: ", "Attach Count: "};
    private String[] titleFarsi = {"صورت تنخواه", "تنخواه گردان: ", "شماره گزارش: ", "تاریخ شروع: ", "تاریخ پایان: ", "تاریخ گزارش: ", "تعداد ضمایم: "};


    public PdfCreator(Context context) {
        this.context = context;
    }


    public void createSalaryReportPdf(PdfLanguage pdfLanguage, List<AWork> workList, List<APicture> pictureList, String startDate, String endDate, AReport aReport) {

        TextProcessing textProcessing = new TextProcessing();

        float[] pointColumnWidths = {20F, 50F, 100F};
        String[] headers;
        String[] titles;
        if (pdfLanguage.equals(PdfLanguage.English)) {
            headers = headerEnglish;
            titles = titleEnglish;
        } else {
            headers = headerFarsi;
            titles = titleFarsi;
        }

        Document document = new Document(PageSize.A4);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "test-2.pdf";
        File filePath = new File(targetPdf);
        try {

            BaseFont myFont = BaseFont.createFont("res/font/yas.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font paraFont = new Font(myFont, 12);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();


            // initialize Title of PDF

            PdfPTable tableTitle = new PdfPTable(1);
            PdfPCell titleCell = new PdfPCell();
            titleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setVerticalAlignment(Element.ALIGN_CENTER);
            titleCell.setPhrase(new Phrase(titles[0], paraFont));
            titleCell.setPadding(20);
            tableTitle.addCell(titleCell);
            tableTitle.completeRow();
            document.add(tableTitle);


            // initialize name of person is reported and report's number

            PdfPTable tablePerson = new PdfPTable(2);
            titleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            titleCell.setVerticalAlignment(Element.ALIGN_CENTER);
            titleCell.setPhrase(new Phrase(titles[1] + "امیر عباس محرری", paraFont));
            titleCell.setPadding(10);
            tablePerson.addCell(titleCell);
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            titleCell.setVerticalAlignment(Element.ALIGN_CENTER);
            titleCell.setPhrase(new Phrase(titles[2] + aReport.getNumber(), paraFont));
            titleCell.setPadding(10);
            tablePerson.addCell(titleCell);
            tablePerson.completeRow();
            document.add(tablePerson);


            // initialize report's date

            PdfPTable dateTable = new PdfPTable(1);
            titleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            titleCell.setVerticalAlignment(Element.ALIGN_CENTER);
            titleCell.setPhrase(new Phrase(titles[5] + aReport.getStringIranianDate(), paraFont));
            titleCell.setPadding(10);
            dateTable.addCell(titleCell);
            dateTable.completeRow();
            document.add(dateTable);


            // initialize start of date and end of date and attach count

            PdfPTable attachTable = new PdfPTable(3);
            titleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            titleCell.setVerticalAlignment(Element.ALIGN_CENTER);
            titleCell.setPhrase(new Phrase(titles[6] + aReport.getAttachCount(), paraFont));
            titleCell.setPadding(10);
            attachTable.addCell(titleCell);
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            titleCell.setPhrase(new Phrase(titles[4] + endDate, paraFont));
            attachTable.addCell(titleCell);
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            titleCell.setPhrase(new Phrase(titles[3] + startDate, paraFont));
            attachTable.addCell(titleCell);
            attachTable.completeRow();
            document.add(attachTable);


            PdfPTable salaryTable = new PdfPTable(headers.length);

            for (String header : headers) {
                PdfPCell cell = new PdfPCell();
                cell.setGrayFill(0.9f);
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(new Phrase(header, paraFont));
                cell.setPadding(10);

                salaryTable.addCell(cell);
            }

            salaryTable.completeRow();


            for (int i = 0; i < workList.size(); i++) {
                PdfPCell cell = new PdfPCell();
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                cell.setPhrase(new Phrase(workList.get(i).getComment(), paraFont));
                salaryTable.addCell(cell);
                cell.setPhrase(new Phrase(NumberTextWatcherForThousand.getDecimalFormattedString(String.valueOf(workList.get(i).getPrice())), paraFont));
                salaryTable.addCell(cell);
                cell.setPhrase(new Phrase(workList.get(i).getName(), paraFont));
                salaryTable.addCell(cell);
                cell.setPhrase(new Phrase(textProcessing.convertDateToStringWithoutTime(workList.get(i).getIranianDate()), paraFont));
                salaryTable.addCell(cell);
                cell.setPhrase(new Phrase(String.valueOf(workList.get(i).getNumber()), paraFont));
                salaryTable.addCell(cell);
                salaryTable.completeRow();
            }


            document.addTitle("my Pdf");
            document.add(salaryTable);

            document.newPage();

            PdfPTable pictureTable = new PdfPTable(1);

            Image bgImage;
            for (int i = 0; i < pictureList.size(); i++) {

                bgImage = Image.getInstance(pictureList.get(i).getPicture());
                bgImage.setAlignment(Element.ALIGN_CENTER);
                bgImage.setScaleToFitHeight(true);
                bgImage.setScaleToFitLineWhenOverflow(true);
                bgImage.setAbsolutePosition(100,250);
                PdfPCell cell2 = new PdfPCell();
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.addElement(bgImage);
                cell2.setVerticalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                pictureTable.addCell(cell2);
                pictureTable.completeRow();

            }

            document.add(pictureTable);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }


    }


}

