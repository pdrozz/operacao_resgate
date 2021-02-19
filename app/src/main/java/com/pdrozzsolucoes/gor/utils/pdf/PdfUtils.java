package com.pdrozzsolucoes.gor.utils.pdf;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.model.fichaTratamento.FichaTratamentoModel;
import com.pdrozzsolucoes.gor.model.fichaTratamento.FichaTratamentoViewModel;
import com.pdrozzsolucoes.gor.model.fichaTratamento.MedicamentoModel;
import com.pdrozzsolucoes.gor.model.novaOcorrencia.NovaOcorrenciaModel;
import com.pdrozzsolucoes.gor.model.orientacoes.OrientacoesModel;
import com.pdrozzsolucoes.gor.utils.AnalyticsUtil;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;

public class PdfUtils {

    private static Font fontBold;
    private static Font fontUnderline;
    private static PdfPCell defaultCell;

    private static void config(){
        fontBold=new Font();
        fontUnderline=new Font();
        fontBold.setStyle(Font.BOLD);
        fontUnderline.setStyle(Font.UNDERLINE);

        defaultCell=new PdfPCell();
    }

    public static void check(Activity activity){
        return;
    }

    public static String getData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

    public static String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(new Date());
    }

    public static String getMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(new Date());
    }

    public static String getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(new Date());
    }

    public static String makeFichaTratamento(FichaTratamentoModel model, Activity activity, String assinatura){

        try{
            AnalyticsUtil.savePdfGerado(activity,AnalyticsUtil.generateFichaTratamento);
        }catch(Exception e){}


        try {
            config();
            File docsFolder = StorageUtil.getPdfPath(activity);
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
            }

            String pdfname = "Ficha Tratamento " +model.getNumeroFicha().replace("/","-") + ".pdf";
            File pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document(PageSize.A4);
            //595,842
            PdfWriter pdfWriter=PdfWriter.getInstance(document, output);
            document.open();

            fontBold.setSize(18);
            Paragraph paragraphTitle=new Paragraph("Ficha de Tratamento",fontBold);
            paragraphTitle.setAlignment(Paragraph.ALIGN_CENTER);
            config();

            document.add(paragraphTitle);
            Paragraph paragraph=new Paragraph("nº: "+model.getNumeroFicha());
            paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(paragraph);

            document.add(new Paragraph("\n"));

            switch (model.getEspecie()){
                case FichaTratamentoViewModel.CAO:
                    document.add(
                            new Paragraph("Espécie: "+
                                    activity.getString(R.string.fichaTratamentoEspecieCanina)
                                            +"___________________"));
                    break;
                case FichaTratamentoViewModel.GATO:
                    document.add(new Paragraph("Espécie: "+activity.getString(R.string.fichaTratamentoEspecieFelina)
                                            +"___________________"));
                    break;
            }
            if(model.getEquino()){
                Paragraph p=new Paragraph("Espécie: "+activity.getString(R.string.fichaTratamentoEspecieEquina)+"  ");
                p.add(new Phrase(model.getEspecie()+"   .",fontUnderline));
                document.add(p);
            }
            Paragraph p=new Paragraph(activity.getString(R.string.fichaTratamentoSexoFemea));
            p.add(new Phrase("        "+"Pelagem: "));
            p.add(new Phrase(" "+model.getPelagem()+"      .",fontUnderline));
            document.add(p);

            p=new Paragraph("Características: ");
            p.add(new Phrase(model.getCaract()+"    .",fontUnderline));
            document.add(p);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Data início tratamento: "+model.getDataInicio()));
            p=new Paragraph("Anammese/suspeita clínica: ");
            p.add(new Phrase(model.getSuspeitaClinica()+"   .",fontUnderline));
            document.add(p);

            document.add(new Paragraph("\n"));
            p=new Paragraph("Procedimentos:  ");
            p.add(new Phrase(model.getProcedimentos()+"      .",fontUnderline));
            document.add(p);
            document.add(new Paragraph("\n"));

            PdfPTable table=new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3,1,1,1,1,1});
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            //for(PdfPCell cell:table.getRow(0).getCells()){
            //    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          //  }
            table.addCell(new Paragraph("Medicamento",fontBold));
            table.addCell(new Paragraph("Qntd",fontBold));
            table.addCell(new Paragraph("Dose",fontBold));
            table.addCell(new Paragraph("Via",fontBold));
            table.addCell(new Paragraph("Duração",fontBold));
            table.addCell(new Paragraph("Período",fontBold));

            for (int i=1;i<=6;i++){
                table.addCell(new Paragraph("  "));
            }
            for(MedicamentoModel item:model.getListItem()){
                table.addCell(item.getMedicamento());
                table.addCell(item.getQnt());
                table.addCell(item.getDose());
                table.addCell(item.getVia());
                table.addCell(item.getDuração());
                table.addCell(item.getPeríodo());
            }
            for (int i=0;i<=6;i++){
                table.addCell(new Paragraph("  "));
            }

            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Data alta clínica: "+model.getDataAlta()));

            if(model.getObito()){
                document.add(new Paragraph("Se óbito, data e causa da morte: "+model.getDataObito()));
                p=new Paragraph(model.getCausaObito()+"    .",fontUnderline);
                document.add(p);
            }
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Data e local da destinação: "+model.getDataDestino()));

            switch (model.getDestino()){
                case FichaTratamentoViewModel.RETORNO:
                    document.add(new Paragraph("( X ) Retornado ao local do resgate"));
                    document.add(new Paragraph("( ) Doação"));
                    document.add(new Paragraph("( ) _______________________________ "));
                    break;
                case FichaTratamentoViewModel.DOACAO:
                    document.add(new Paragraph("(  ) Retornado ao local do resgate"));
                    document.add(new Paragraph("( X ) Doação"));
                    document.add(new Paragraph("( ) _______________________________ "));
                    break;
            }
            if (model.getOutroDestino()){
                document.add(new Paragraph("(  ) Retornado ao local do resgate"));
                document.add(new Paragraph("(  ) Doação"));
                p=new Paragraph("( X ) ");
                p.add(new Phrase(model.getDestino()+"    .",fontUnderline));
                document.add(p);
            }



            if(assinatura==null){
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("\n"));
                p=new Paragraph("____________________________");
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);

                p=new Paragraph("Médico(A) Veterinário (a)");
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);

                p=new Paragraph("(CARIMBO E ASSINATURA)");
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
            }else{
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("\n"));
                Image image=Image.getInstance(getImageFromStorageResized(new File(assinatura),200));
                image.setAlignment(Element.ALIGN_RIGHT);
                document.add(image);
                p=new Paragraph("Médico(A) Veterinário (a)");
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
                p=new Paragraph("(CARIMBO E ASSINATURA)");
                p.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(p);
            }

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(activity.getString(R.string.fichaTratamentoRodape)));


            File anexo=new File(StorageUtil.getFichaAnexo(activity),"anexo.png");
            if (anexo!=null && anexo.exists()){
                document.newPage();
                PdfPTable tableImagens=new PdfPTable(1);
                tableImagens.setWidthPercentage(100);
                tableImagens.setWidths(new int[]{1});
                //tableImagens.getDefaultCell().setBorder((Rectangle.NO_BORDER));
                tableImagens.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                tableImagens.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                Image image=Image.getInstance(getImageFromStorageResized(activity,anexo,480));
                PdfPCell cellImagem = new PdfPCell(image,false);
                cellImagem.setBorder(Rectangle.NO_BORDER);
                cellImagem.setPadding(2);
                tableImagens.addCell(cellImagem);

                fontBold.setSize(22);
                document.add(new Paragraph("Anexo:",fontBold));
                document.add(new Paragraph());
                document.add(tableImagens);
            }


            document.close();
            return pdfFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            StorageUtil.getAssinaturaPath(activity).delete();
            StorageUtil.getFichaAnexo(activity).delete();
        }
        return "";
        }

    public static String makeNovaOcorrência(NovaOcorrenciaModel model,Activity activity) {

        try{
            AnalyticsUtil.savePdfGerado(activity,AnalyticsUtil.generateOcorrencia);
        }catch(Exception e){}

        try {
            File docsFolder = StorageUtil.getPdfPath(activity);
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
            }

            String pdfname = "Ocorrencia_" +model.getNumeroOcorrencia().replace("/","-") + ".pdf";
            File pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document(PageSize.A4);
            //595,842);
            PdfWriter pdfWriter=PdfWriter.getInstance(document, output);
            document.open();

            PdfContentByte pdfContentByte=pdfWriter.getDirectContent();
            Rectangle rec=new Rectangle(0,845,595,670);
            rec.setBackgroundColor(new BaseColor(234,236,249));
            pdfContentByte.rectangle(rec);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 5});

            table.getDefaultCell().setFixedHeight(130);

            table.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPTable table1=new PdfPTable(1);
            table1.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Font catFont = new Font();
            catFont.setStyle(Font.BOLD);
            catFont.setSize(18);
            catFont.setColor(new BaseColor(196, 57, 25));
            table1.addCell(new Paragraph("Central de Ocorrências GOR",catFont));
            table1.addCell("\n");
            table1.addCell("\n");
            catFont=new Font();
            catFont.setStyle(Font.BOLD);
            catFont.setSize(14);
            catFont.setColor(new BaseColor(76, 81, 99));
            table1.addCell(new Paragraph("Grupo de Operações e Resgate\n",catFont));
            table1.addCell(new Paragraph("Telefone: (47) 99651-9961",catFont));
            catFont.setSize(12);
            table1.addCell(new Paragraph("E-mail: grupodeoperacoeseseresgategor@gmail.com",catFont));

            Image logo=Image.getInstance(getLogoImageFromAsset(activity));
            //logo.scaleToFit(180,50);
            PdfPCell cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(logo);
            table.addCell(table1);


            document.add(table);

            PdfPTable tableContent=new PdfPTable(3);
            tableContent.setWidthPercentage(100);
            tableContent.setWidths(new int[]{1,1,1});
            tableContent.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            tableContent.getDefaultCell().setPaddingLeft(4);
            tableContent.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            tableContent.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cellText=new PdfPCell();
            cellText.setBorder(Rectangle.NO_BORDER);
            cellText.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cellContent=new PdfPCell();
            cellContent.setBorder(Rectangle.NO_BORDER);
            cellContent.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellContent.setColspan(2);

            Font fontBold=new Font();
            fontBold.setStyle(Font.BOLD);

            cellContent=configCellContent();
            cellText=configCellTitle();
            List<String> listStrings=new ArrayList<>();
            if(model.getResponsavel()!=null && !model.getResponsavel().equals("")){
                Paragraph p=new Paragraph();
                for (String e:model.getResponsavel().split(",")){
                    listStrings.add(e);
                    Font f=new Font();
                    Chunk c=new Chunk("  "+e+"   ");
                    c.setBackground(new BaseColor(234,236,249));
                    Chunk space=new Chunk("  ");
                    p.add(c);
                    p.add(space);
                }
                cellContent.addElement(p);
                cellText.addElement(new Paragraph("Responsável pela Operação",fontBold));
                tableContent=addCellInTable(tableContent,cellText,cellContent);
            }

            cellContent=configCellContent();
            cellText=configCellTitle();

            listStrings=new ArrayList<>();
            if(model.getVonluntaio()!=null && !model.getVonluntaio().equals("")){
                Paragraph p=new Paragraph();
                for (String e:model.getVonluntaio().split(",")){
                    listStrings.add(e);
                    Font f=new Font();
                    Chunk c=new Chunk("  "+e+"   ");
                    c.setBackground(new BaseColor(234,236,249));
                    Chunk space=new Chunk("  ");
                    p.add(c);
                    p.add(space);
                }
                cellContent.addElement(p);
                cellText.addElement(new Paragraph("Voluntário(a) ",fontBold));
                tableContent=addCellInTable(tableContent,cellText,cellContent);
            }


            //numero ocorrencia
            cellContent=configCellContent();
            cellText=configCellTitle();

            cellText.addElement(new Paragraph("Ocorrência de Nº:",fontBold));
            cellContent.addElement(new Paragraph(model.getNumeroOcorrencia()));
            tableContent=addCellInTable(tableContent,cellText,cellContent);
            //data
            cellContent=configCellContent();
            cellText=configCellTitle();

            cellText.addElement(new Paragraph("Data",fontBold));
            cellContent.addElement(new Paragraph(getData()));//model.getData()));
            tableContent=addCellInTable(tableContent,cellText,cellContent);
            //gerada
            if(!model.getGerada().equals("")){
            cellContent=configCellContent();
            cellText=configCellTitle();

            cellText.addElement(new Paragraph("Ocorrência gerada às:",fontBold));
            cellContent.addElement(new Paragraph(model.getGerada()));//model.getData()));
            tableContent=addCellInTable(tableContent,cellText,cellContent);
            }
            //finalizada
            if(!model.getFinalizada().equals("")) {
                cellContent = configCellContent();
                cellText = configCellTitle();

                cellText.addElement(new Paragraph("Ocorrência finalizada às:", fontBold));
                cellContent.addElement(new Paragraph(model.getFinalizada()));//model.getData()));
                tableContent = addCellInTable(tableContent, cellText, cellContent);
            }
            Chunk ck;
            //tipo ocorrencia
            if(!model.getTipoOcorrencia().equals("")) {
                cellContent = configCellContent();
                cellText = configCellTitle();

                cellText.addElement(new Paragraph("Tipo da Ocorrência:", fontBold));
                ck = new Chunk(" - " + model.getTipoOcorrencia() + " - ");
                ck.setBackground(new BaseColor(234, 236, 249));
                cellContent.addElement(ck);//model.getData()));
                tableContent = addCellInTable(tableContent, cellText, cellContent);
            }
            //origem ocorrencia
            if(!model.getOrigemOcorrencia().equals("")) {
                cellContent = configCellContent();
                cellText = configCellTitle();

                cellText.addElement(new Paragraph("Origem da Ocorrência:", fontBold));
                ck = new Chunk(" - " + model.getOrigemOcorrencia() + " - ");
                ck.setBackground(new BaseColor(234, 236, 249));
                cellContent.addElement(ck);//model.getData()));
                tableContent = addCellInTable(tableContent, cellText, cellContent);
            }
            //endereco
            if(!model.getEndereco().equals("")) {
                cellContent = configCellContent();
                cellText = configCellTitle();

                cellText.addElement(new Paragraph("Endereço:", fontBold));
                cellContent.addElement(new Paragraph(model.getEndereco()));//model.getData()));
                tableContent = addCellInTable(tableContent, cellText, cellContent);
            }
            //desc
            if(!model.getDescricao().equals("")) {
                cellContent = configCellContent();
                cellText = configCellTitle();

                cellText.addElement(new Paragraph("Descrição da Ocorrência:", fontBold));
                cellContent.addElement(new Paragraph(model.getDescricao()));//model.getData()));
                tableContent = addCellInTable(tableContent, cellText, cellContent);
            }
            //tipo de animal envolvido
            if(!model.getTipoAnimal().equals("")) {
                cellContent = configCellContent();
                cellText = configCellTitle();

                cellText.addElement(new Paragraph("TIPO DO ANIMAL ENVOLVIDO:", fontBold));
                cellContent.addElement(new Paragraph(model.getTipoAnimal()));//model.getData()));
                tableContent = addCellInTable(tableContent, cellText, cellContent);
            }
            //telefone
            if(!model.getTelefone().equals("")) {
                cellContent=configCellContent();
                cellText=configCellTitle();

                cellText.addElement(new Paragraph("Telefone",fontBold));
                cellContent.addElement(new Paragraph(model.getTelefone()));//model.getData()));
                tableContent=addCellInTable(tableContent,cellText,cellContent);
            }

            document.add(new Paragraph("\n"));
            document.add(tableContent);

            PdfPTable tableImagens=new PdfPTable(2);
            tableImagens.setWidthPercentage(100);
            tableImagens.setWidths(new int[]{1,1});
            //tableImagens.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            tableImagens.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            tableImagens.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            String TAG="tag makeocor";
            ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
            File directory = StorageUtil.getAnexoPath(activity);
            File[] files=(directory.listFiles());
            int qntImagens=0;
            if (files!=null)
            {
                if(files.length>0){
                for (File file : files) {
                    Image image=Image.getInstance(getImageFromStorageResized(activity,file));
                    PdfPCell cellImagem = new PdfPCell(image,true);
                    cellImagem.setBorder(Rectangle.NO_BORDER);
                    cellImagem.setPadding(2);
                    tableImagens.addCell(cellImagem);
                    qntImagens++;
                    Log.i(TAG, "makeNovaOcorrência: ADICIONANDO IMAGEM");
                }


                if(qntImagens%2!=0) {
                    tableImagens.addCell(new Paragraph(""));
                }
                fontBold.setSize(22);
                document.add(new Paragraph("Fotos:",fontBold));
                document.add(new Paragraph(""));
                document.add(tableImagens);
            }
            }
            /*PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setWidths(new int[]{5, 1, 2, 4});
            table1.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            table1.getDefaultCell().setPaddingLeft(4);
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);


            table1.addCell("");
            table1.addCell("Ok");
            table1.addCell(" Não");
            table1.addCell("       OBS       ");

            document.add(table);*/
            //document.add(new Paragraph("\n"));


            //Image imageAssinaturaCliente=null; //= Image.getInstance(getByteFromFilePathResized(assinaturaPath));
          //  document.add(imageAssinaturaCliente);

            document.close();
            return pdfFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    public static PdfPTable addCellInTable(PdfPTable table,PdfPCell cellTitle,PdfPCell cellContent){
        table.addCell(cellTitle);
        table.addCell(cellContent);
        return table;
    }

    public static String makeOrientacoes(OrientacoesModel model, Activity activity){

        try{
            AnalyticsUtil.savePdfGerado(activity,AnalyticsUtil.generateOrientacoes);
        }catch(Exception e){}


        try {
            File docsFolder = StorageUtil.getPdfPath(activity);
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
            }

            String pdfname =( "Orientacoes "+model.getNumeroOcorrencia()+" data "+model.getData()+" " + model.getNomeProprietario().replace("/", "-")+" "+model.getEndereco() + ".pdf").replace("/","-");
            File pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document(new RectangleReadOnly(595,882));
            document.setMargins(document.leftMargin()/1.5f,document.rightMargin()/1.5f,8,8);
            //595,842);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, output);
            document.open();


            ///start header
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 5});

            table.getDefaultCell().setFixedHeight(130);
            table.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPTable table1=new PdfPTable(1);
            table1.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Font catFont = new Font();
            catFont.setStyle(Font.BOLD);
            catFont.setSize(18);
            catFont.setColor(new BaseColor(196, 57, 25));
            table1.addCell("\n");
            Paragraph paragraph=new Paragraph("GRUPO DE OPERAÇÕES E RESGATE\nGOR",catFont);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            table1.addCell(paragraph);
            table1.addCell("\n");
            catFont=new Font();
            catFont.setStyle(Font.BOLD);
            catFont.setSize(14);
            catFont.setColor(new BaseColor(76, 81, 99));
            table1.addCell(new Paragraph("CNPJ 25.070.951/0001-68",catFont));
            table1.addCell(new Paragraph("Telefone: (47) 99651-9961",catFont));
            catFont.setSize(12);
            table1.addCell(new Paragraph("E-mail: grupodeoperacoeseseresgategor@gmail.com",catFont));

            Image logo=Image.getInstance(getLogoImageFromAsset(activity));
            PdfPCell cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(logo);
            table.addCell(table1);
            document.add(table);
            ///end header

            ///start top
            catFont.setSize(14);
            catFont.setColor(new BaseColor(0,0,0));
            Paragraph paragraphTitle=new Paragraph("Orientações para bem-estar do animal".toUpperCase(),catFont);
            paragraphTitle.setSpacingBefore(-6);
            paragraphTitle.setSpacingAfter(4);
            paragraphTitle.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraphTitle);

            PdfPTable tableHeader = new PdfPTable(2);
            tableHeader.setWidthPercentage(95);
            tableHeader.setWidths(new int[]{1, 1});
            tableHeader.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            Font fontPadrao=new Font();
            Paragraph p=new Paragraph("Nome do Animal: "+model.getNomeAnimal());
            PdfPCell cellHeader=new PdfPCell(p);
            cellHeader.setBorder(Rectangle.NO_BORDER);
            tableHeader.addCell(cellHeader);
            switch (model.getTipoAnimal()){
                case OrientacoesModel.CAO:
                    tableHeader.addCell("Tipo de animal: Cão ( X ) Gato ( ) Equino ( ) ");
                    break;
                case OrientacoesModel.GATO:
                    tableHeader.addCell("Tipo de animal: Cão ( ) Gato ( X ) Equino ( ) ");
                    break;
                case OrientacoesModel.EQUINO:
                    tableHeader.addCell("Tipo de animal: Cão ( ) Gato ( ) Equino ( X ) ");
                    break;
                default:
                    tableHeader.addCell("Tipo de animal: Cão ( ) Gato ( ) Equino ( ) ");
                    break;
            }
            //proprietario
            fontPadrao=new Font();
            p=new Paragraph("Proprietário: "+model.getNomeProprietario());
            cellHeader=new PdfPCell(p);
            cellHeader.setBorder(Rectangle.NO_BORDER);

            tableHeader.addCell(cellHeader);
            tableHeader.addCell(new Paragraph());
            //data
            fontPadrao=new Font();
            p=new Paragraph("Data: "+model.getData() +" Hora: "+model.getHora() );
            cellHeader=new PdfPCell(p);
            cellHeader.setBorder(Rectangle.NO_BORDER);
            tableHeader.addCell(cellHeader);
            tableHeader.addCell(new Paragraph());

            //data
            fontPadrao=new Font();
            p=new Paragraph("Endereço: "+model.getEndereco());
            cellHeader=new PdfPCell(p);
            cellHeader.setBorder(Rectangle.NO_BORDER);
            cellHeader.setColspan(2);
            tableHeader.addCell(cellHeader);

            document.add(tableHeader);
            Paragraph space=new Paragraph();
            space.setSpacingAfter(4);
            document.add(space);

            PdfPTable tableBody=new PdfPTable(2);
            tableBody.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            tableBody.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableBody.setWidths(new int[]{1,1});
            tableBody.setWidthPercentage(100);

            PdfPTable tableContent=new PdfPTable(1);
            tableContent.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            tableContent.setWidths(new int[]{1});
            tableContent.setWidthPercentage(97);

            fontPadrao=new Font();
            fontPadrao.setStyle(Font.BOLD);
            tableContent.addCell(new Paragraph("Nutrição",fontPadrao));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkNutricao1),model.isNutricao1()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkNutricao2),model.isNutricao2()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkNutricao3),model.isNutricao3()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkNutricao4),model.isNutricao4()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkNutricao5),model.isNutricao5()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkNutricao6),model.isNutricao6()));

            tableContent.addCell(new Paragraph());
            tableContent.addCell(new Paragraph("Conforto",fontPadrao));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkConforto1),model.isConforto1()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkConforto2),model.isConforto2()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkConforto3),model.isConforto3()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkConforto4),model.isConforto4()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkConforto5),model.isConforto5()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkConforto6),model.isConforto6()));

            tableContent.addCell(new Paragraph("OBS",fontPadrao));
            tableContent.addCell(model.getObs());

            tableBody.addCell(tableContent);


            tableContent=new PdfPTable(1);
            tableContent.getDefaultCell().setBorder((Rectangle.NO_BORDER));
            tableContent.setWidths(new int[]{1});
            tableContent.setWidthPercentage(97);


            tableContent.addCell(new Paragraph("Sanidade",fontPadrao));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkNutricao1),model.isSanidade1()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkSanidade2),model.isSanidade2()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkSanidade3),model.isSanidade3()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkSanidade4),model.isSanidade4()));

            tableContent.addCell(new Paragraph("Comportamento",fontPadrao));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkComportamento1),model.isComportamento1()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkComportamento2),model.isComportamento2()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkComportamento3),model.isComportamento3()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkComportamento4),model.isComportamento4()));
            tableContent.addCell(checkOrientacoes(activity.getString(R.string.checkComportamento5),model.isComportamento5()));

            tableBody.addCell(tableContent);
            document.add(tableBody);
            fontPadrao=new Font();
            fontPadrao.setStyle(Font.BOLD);
            paragraphTitle=new Paragraph("Termo de comprometimento",fontPadrao);
            paragraphTitle.setAlignment(Paragraph.ALIGN_CENTER);
            paragraphTitle.setSpacingAfter(4);
            document.add(paragraphTitle);

            Paragraph endParagraph=new Paragraph();
            endParagraph.add(new Phrase("Eu, "));
            endParagraph.add(new Phrase(model.getNomeCompletoProprietario()+", ",fontUnderline));
            endParagraph.add(new Phrase("portador(a) do CPF sob número: "));
            endParagraph.add(new Phrase(model.getCpf()+", ",fontUnderline));
            endParagraph.add(new Phrase("nascido em "));
            endParagraph.add(new Phrase(model.getDataNascimento()+"  ,",fontUnderline));
            endParagraph.add(new Phrase(" comprometendo-me a realizar continuamente a Guarda Responsável do meu animal de companhia, "+
            "segundo as orientações recebidas durante a visita do GOR."));
            document.add(endParagraph);

            LineSeparator lineAssinatura=new LineSeparator();
            //lineAssinatura.setPercentage(30);
            lineAssinatura.setLineWidth(30);
            lineAssinatura.setAlignment(LineSeparator.ALIGN_LEFT);
            Image image;
            if (model.isProprietarioAssinou()){
                document.add(new Paragraph("( X ) Responsável recusa a assinar o Termo de Comprometimento."));
            }
            else {
                document.add(new Paragraph("( ) Responsável recusa a assinar o Termo de Comprometimento."));
            }
            if (model.getAssinaturaResponsavel()==null || model.getAssinaturaResponsavel().length()<2){
                document.add(new Paragraph("_______________________________________________________"));
                document.add(new Paragraph("ASSINATURA DO RESPONSÁVEL PELO ANIMAL"));
            }else {
                image=Image.getInstance(getImageFromStorageResized(new File(model.getAssinaturaResponsavel()),110,100));
                image.setAlignment(Element.ALIGN_LEFT);
                image.setBorder(Rectangle.BOTTOM);
                document.add(image);
                Paragraph line=new Paragraph("_______________________________________________________");
                line.setSpacingAfter(0);
                line.setSpacingBefore(0);
                document.add(line);
                document.add(new Paragraph("ASSINATURA DO RESPONSÁVEL PELO ANIMAL"));
            }
            if (model.getAssinaturaOperacao()==null || model.getAssinaturaOperacao().length()<2){
                document.add(new Paragraph("_______________________________________________________"));
                document.add(new Paragraph("ASSINATURA DO RESPONSÁVEL PELA OCORRÊNCIA"));
            }else{
                image=Image.getInstance(getImageFromStorageResized(new File(model.getAssinaturaOperacao()),110,100));
                image.setAlignment(Element.ALIGN_LEFT);
                image.setBorder(Rectangle.BOTTOM);
                document.add(image);
                Paragraph line=new Paragraph("_______________________________________________________");
                line.setSpacingAfter(0);
                line.setSpacingBefore(0);
                document.add(line);
                document.add(new Paragraph("ASSINATURA DO RESPONSÁVEL PELA OCORRÊNCIA"));
            }

            document.close();
            return pdfFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    private static String checkOrientacoes(String content,boolean bool){
        if (bool){
            return "(X) "+content;
        }else{
            return "( ) "+content;
        }
    }

    private  static byte[] getLogoImageFromAsset(Activity activity) {
        try {

            String fileName = "logo470width.png";

            InputStream ims = activity.getAssets().open(fileName);

            Bitmap bmp = BitmapFactory.decodeStream(ims);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            return stream.toByteArray();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            return null;
        }

    }

    public static PdfPCell configCellContent(){
        PdfPCell cellContent=new PdfPCell();
        cellContent.setPaddingLeft(50);
        cellContent.setPaddingRight(10);
        cellContent.setBorder(Rectangle.NO_BORDER);
        cellContent.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellContent.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellContent.setColspan(2);
        return cellContent;
    }
    public static PdfPCell configCellTitle(){
        PdfPCell cellContent=new PdfPCell();
        cellContent.setBorder(Rectangle.NO_BORDER);
        cellContent.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellContent.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cellContent;
    }

    private static byte[] getImageFromStorage(Activity ac,File path){
        try {
            File anex=new File(path.getAbsolutePath());
            Bitmap bitmapAnexo=BitmapFactory.decodeStream(new FileInputStream(anex));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapAnexo.compress(Bitmap.CompressFormat.PNG, 60, stream);
            return stream.toByteArray();
        }
        catch(Exception ex)
        {
            Log.i("TAG", "getImageFromStorage: makeNovaOcorrência Erro ao recupearrar");
            ex.printStackTrace();
            Log.i("TAG", "getImageFromStorage: message"+ ex.getMessage());
            return null;
        }

    }

    private static byte[] getImageFromStorageResized(Activity ac,File path){
        try {
            File anex=new File(path.getAbsolutePath());
            Bitmap bitmapAnexo=BitmapFactory.decodeStream(new FileInputStream(anex));
            Bitmap resized=scaleDown(bitmapAnexo,1080,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
        catch(Exception ex)
        {
            Log.i("TAG", "getImageFromStorage: makeNovaOcorrência Erro ao recupearrar");
            ex.printStackTrace();
            Log.i("TAG", "getImageFromStorage: message"+ ex.getMessage());
            return null;
        }
    }

    private static byte[] getImageFromStorageResized(Activity ac,File path,int max){
        try {
            File anex=new File(path.getAbsolutePath());
            Bitmap bitmapAnexo=BitmapFactory.decodeStream(new FileInputStream(anex));
            Bitmap resized=scaleDown(bitmapAnexo,max,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
        catch(Exception ex)
        {
            Log.i("TAG", "getImageFromStorage: makeNovaOcorrência Erro ao recupearrar");
            ex.printStackTrace();
            Log.i("TAG", "getImageFromStorage: message"+ ex.getMessage());
            return null;
        }
    }

    private static byte[] getImageFromStorageResized(File path,int maxHeight){
        try {
            File anex=new File(path.getAbsolutePath());
            Bitmap bitmapAnexo=BitmapFactory.decodeStream(new FileInputStream(anex));
            Bitmap resized=scaleDown(bitmapAnexo,maxHeight,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 50, stream);
            return stream.toByteArray();
        }
        catch(Exception ex)
        {
            Log.i("TAG", "getImageFromStorage: makeNovaOcorrência Erro ao recupearrar");
            ex.printStackTrace();
            Log.i("TAG", "getImageFromStorage: message"+ ex.getMessage());
            return null;
        }
    }

    private static byte[] getImageFromStorageResized(File path,int maxHeight,int quality){
        try {
            File anex=new File(path.getAbsolutePath());
            Bitmap bitmapAnexo=BitmapFactory.decodeStream(new FileInputStream(anex));
            Bitmap resized=scaleDown(bitmapAnexo,maxHeight,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, quality, stream);
            return stream.toByteArray();
        }
        catch(Exception ex)
        {
            Log.i("TAG", "getImageFromStorage: makeNovaOcorrência Erro ao recupearrar");
            ex.printStackTrace();
            Log.i("TAG", "getImageFromStorage: message"+ ex.getMessage());
            return null;
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        if (ratio>=1){
            return realImage;
        }
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
