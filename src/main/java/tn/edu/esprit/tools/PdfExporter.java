package tn.edu.esprit.tools;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import tn.edu.esprit.entities.CarnetEducatif;
import tn.edu.esprit.entities.Commentaire;
import tn.edu.esprit.services.ServiceCommentaire;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PdfExporter {

    /* ── Couleurs ── */
    private static final BaseColor VIOLET      = new BaseColor(102, 126, 234);
    private static final BaseColor VIOLET_DARK = new BaseColor(118,  75, 162);
    private static final BaseColor VERT        = new BaseColor( 17, 153, 142);
    private static final BaseColor VERT_LIGHT  = new BaseColor(220, 248, 244);
    private static final BaseColor ORANGE      = new BaseColor(247, 151,  30);
    private static final BaseColor ROUGE       = new BaseColor(255,  65, 108);
    private static final BaseColor BLEU_CLAIR  = new BaseColor( 79, 172, 254);
    private static final BaseColor GRIS_LIGHT  = new BaseColor(245, 247, 255);
    private static final BaseColor GRIS_DARK   = new BaseColor( 90, 106, 138);
    private static final BaseColor BLANC       = BaseColor.WHITE;
    private static final BaseColor NOIR        = new BaseColor( 30,  30,  60);
    private static final ServiceCommentaire serviceCommentaire = new ServiceCommentaire();

    /* ── Polices ── */
    private static Font fTitre(int s)         { return new Font(Font.FontFamily.HELVETICA, s,  Font.BOLD,   BLANC); }
    private static Font fSection()            { return new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,   VIOLET_DARK); }
    private static Font fLabel()              { return new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   GRIS_DARK); }
    private static Font fValeur()             { return new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, NOIR); }
    private static Font fBadge(BaseColor c)   { return new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   c); }
    private static Font fTHead()              { return new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   BLANC); }
    private static Font fTCell()              { return new Font(Font.FontFamily.HELVETICA,  8, Font.NORMAL, NOIR); }

    /* ==========================================================
     *  exporterCarnet() — fiche individuelle complète
     * ========================================================== */
    public static void exporterCarnet(CarnetEducatif c, String chemin) throws Exception {

        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(chemin));
        doc.open();

        header(doc, writer, "Carnet Educatif - FocusKid", "Fiche de suivi individuelle");

        /* ── A. Informations générales ── */
        section(doc, "A. Informations generales");
        PdfPTable t1 = table4col();
        row2(t1, "Date",         str(c.getDateEtude()),
                 "Lieu",         str(c.getLieu()));
        row2(t1, "Heure debut",  heure(c.getHeureDebut()),
                 "Heure fin",    heure(c.getHeureFin()));
        row2(t1, "Duree totale", c.getDureeTotale() + " min",
                 "Matiere",      str(c.getMatiere()));
        doc.add(t1);

        /* ── B. Activité ── */
        section(doc, "B. Activite");
        PdfPTable t2 = table4col();
        row2(t2, "Type activite",   str(c.getTypeActivite()),
                 "Difficulte",      str(c.getNiveauDifficulte()));
        row2(t2, "Travail termine", c.isTravailTermine() ? "Oui" : "Non",
                 "Travaille seul",  c.isTravailleSeul()  ? "Oui" : "Non");
        row2(t2, "Demande aide",    c.isDemandeAide()    ? "Oui" : "Non",
                 "",                "");
        doc.add(t2);

        /* ── C. Comportement ── */
        section(doc, "C. Comportement & Concentration");
        doc.add(Chunk.NEWLINE);
        jauge(doc, writer, "Concentration",                c.getNiveauConcentration(), 5, VERT);
        jauge(doc, writer, "Agitation",                    c.getNiveauAgitation(),     5, ORANGE);
        jauge(doc, writer, "Autonomie",                    c.getNiveauAutonomie(),     5, VIOLET);

        PdfPTable t3 = table4col();
        row2(t3, "Nombre interruptions",      String.valueOf(c.getNombreInterruptions()),
                 "Temps avant perte conc.",   c.getTempsAvantPerteConcentration() + " min");
        doc.add(t3);

        /* ── D. Observations ── */
        section(doc, "D. Observations");
        doc.add(Chunk.NEWLINE);

        carte(doc, "Difficultes rencontrees",
            vide(c.getDifficultes()) ? "(aucune renseignee)" : c.getDifficultes(),
            new BaseColor(255, 245, 245), ROUGE);

        carte(doc, "Points positifs",
            vide(c.getPointsPositifs()) ? "(aucun renseigne)" : c.getPointsPositifs(),
            VERT_LIGHT, VERT);

        /* ── E. Commentaires ── */
        section(doc, "E. Commentaires");
        doc.add(Chunk.NEWLINE);

        List<Commentaire> commentaires = serviceCommentaire.getByCarnetId(c.getId());

        if (commentaires.isEmpty()) {
            doc.add(new Paragraph("Aucun commentaire."));
        } else {
            PdfPTable tableCom = new PdfPTable(3);
            tableCom.setWidthPercentage(100);
            tableCom.setSpacingBefore(5);

            tableCom.addCell("Date");
            tableCom.addCell("Type");
            tableCom.addCell("Commentaire");

            for (Commentaire com : commentaires) {
                tableCom.addCell(String.valueOf(com.getDateCommentaire()));
                tableCom.addCell(com.getTypeCommentaire());
                tableCom.addCell(com.getTexteCommentaire());
            }

            doc.add(tableCom);
        }

        /* ── Footer ── */
        footer(doc);

        /* ✅ FERMER À LA FIN */
        doc.close();
    }
    

    /* ==========================================================
     *  exporterRapport() — rapport analytique (GraphiqueController l'appelle)
     *  Alias vers exporterGraphique() pour compatibilité
     * ========================================================== */
    public static void exporterRapport(List<CarnetEducatif> liste, String chemin) throws Exception {
        exporterGraphique(liste, chemin);
    }

    /* ==========================================================
     *  exporterGraphique() — rapport analytique complet
     * ========================================================== */
    public static void exporterGraphique(List<CarnetEducatif> liste, String chemin) throws Exception {

        if (liste == null || liste.isEmpty()) return;

        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(chemin));
        doc.open();

        header(doc, writer, "Rapport Analytique - FocusKid", "Tableau de bord educatif");

        /* ── KPI ── */
        section(doc, "Resume global");
        doc.add(Chunk.NEWLINE);

        double moyConc  = liste.stream().mapToInt(CarnetEducatif::getNiveauConcentration).average().orElse(0);
        double moyDuree = liste.stream().mapToInt(CarnetEducatif::getDureeTotale).average().orElse(0);
        int    totalInt = liste.stream().mapToInt(CarnetEducatif::getNombreInterruptions).sum();
        double moyAuto  = liste.stream().mapToInt(CarnetEducatif::getNiveauAutonomie).average().orElse(0);

        PdfPTable kpi = new PdfPTable(4);
        kpi.setWidthPercentage(100);
        kpi.setSpacingAfter(14);
        kpiCard(kpi, "Concentration\nmoyenne",  String.format("%.1f / 5", moyConc),  VIOLET);
        kpiCard(kpi, "Duree\nmoyenne / seance", String.format("%.0f min", moyDuree), VERT);
        kpiCard(kpi, "Total\ninterruptions",    String.valueOf(totalInt),             ORANGE);
        kpiCard(kpi, "Autonomie\nmoyenne",      String.format("%.1f / 5", moyAuto),  BLEU_CLAIR);
        doc.add(kpi);

        /* ── Jauges ── */
        section(doc, "Indicateurs moyens");
        doc.add(Chunk.NEWLINE);
        jauge(doc, writer, "Concentration moyenne", (int) Math.round(moyConc), 5, VERT);
        jauge(doc, writer, "Autonomie moyenne",     (int) Math.round(moyAuto), 5, VIOLET);

        /* ── Tableau par matière ── */
        section(doc, "Temps total par matiere");
        doc.add(Chunk.NEWLINE);

        Map<String, Integer> mapMat   = new LinkedHashMap<>();
        Map<String, Integer> mapCount = new LinkedHashMap<>();
        for (CarnetEducatif c : liste) {
            mapMat  .merge(c.getMatiere(), c.getDureeTotale(), Integer::sum);
            mapCount.merge(c.getMatiere(), 1,                  Integer::sum);
        }
        int maxD = mapMat.values().stream().max(Integer::compareTo).orElse(1);

        PdfPTable mt = new PdfPTable(4);
        mt.setWidthPercentage(100);
        mt.setSpacingBefore(4);
        mt.setSpacingAfter(12);
        mt.setWidths(new float[]{2f, 1.2f, 1.2f, 3f});

        for (String h : new String[]{"Matiere", "Seances", "Total (min)", "Progression"}) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fTHead()));
            cell.setBackgroundColor(VIOLET); cell.setPadding(7); cell.setBorder(Rectangle.NO_BORDER);
            mt.addCell(cell);
        }
        int row = 0;
        for (String mat : mapMat.keySet()) {
            BaseColor bg = (row++ % 2 == 0) ? GRIS_LIGHT : BLANC;
            tcell(mt, mat,                          bg, Element.ALIGN_LEFT);
            tcell(mt, String.valueOf(mapCount.get(mat)), bg, Element.ALIGN_CENTER);
            tcell(mt, mapMat.get(mat) + " min",     bg, Element.ALIGN_CENTER);
            PdfPCell bc = new PdfPCell();
            bc.setBackgroundColor(bg); bc.setBorder(Rectangle.NO_BORDER); bc.setPadding(8);
            bc.addElement(barre(writer, (float) mapMat.get(mat) / maxD, VIOLET));
            mt.addCell(bc);
        }
        doc.add(mt);

        /* ── Tableau détail séances ── */
        section(doc, "Detail des seances");
        doc.add(Chunk.NEWLINE);

        PdfPTable st = new PdfPTable(6);
        st.setWidthPercentage(100);
        st.setSpacingBefore(4);
        st.setWidths(new float[]{1.4f, 1.6f, 1.2f, 1.2f, 1.2f, 1.4f});

        for (String h : new String[]{"Date", "Matiere", "Duree", "Conc.", "Agit.", "Type"}) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fTHead()));
            cell.setBackgroundColor(VIOLET_DARK); cell.setPadding(6); cell.setBorder(Rectangle.NO_BORDER);
            st.addCell(cell);
        }
        int r = 0;
        for (CarnetEducatif c : liste) {
            BaseColor bg = (r++ % 2 == 0) ? GRIS_LIGHT : BLANC;
            tcell(st, str(c.getDateEtude()),           bg, Element.ALIGN_CENTER);
            tcell(st, str(c.getMatiere()),              bg, Element.ALIGN_LEFT);
            tcell(st, c.getDureeTotale() + " min",     bg, Element.ALIGN_CENTER);

            BaseColor cc = c.getNiveauConcentration() >= 4 ? VERT
                         : c.getNiveauConcentration() >= 3 ? ORANGE : ROUGE;
            PdfPCell concCell = badge(c.getNiveauConcentration() + "/5", cc, bg);
            st.addCell(concCell);

            BaseColor ac = c.getNiveauAgitation() <= 2 ? VERT
                         : c.getNiveauAgitation() <= 3 ? ORANGE : ROUGE;
            PdfPCell agitCell = badge(c.getNiveauAgitation() + "/5", ac, bg);
            st.addCell(agitCell);

            tcell(st, str(c.getTypeActivite()), bg, Element.ALIGN_LEFT);
        }
        doc.add(st);

        footer(doc);
        doc.close();
    }

    /* ==========================================================
     *  COMPOSANTS INTERNES
     * ========================================================== */

    private static void header(Document doc, PdfWriter writer,
                                String titre, String sous) throws Exception {
        PdfPTable h = new PdfPTable(1);
        h.setWidthPercentage(100);
        h.setSpacingAfter(18);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(VIOLET);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(18);
        String date = "Genere le " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Paragraph p = new Paragraph();
        p.add(new Chunk(titre + "\n", fTitre(20)));
        p.add(new Chunk(sous  + "\n", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC,  new BaseColor(220,225,255))));
        p.add(new Chunk(date,          new Font(Font.FontFamily.HELVETICA,  8, Font.NORMAL, new BaseColor(200,210,255))));
        cell.addElement(p);
        h.addCell(cell);
        doc.add(h);
    }

    private static void section(Document doc, String texte) throws Exception {
        Paragraph p = new Paragraph(texte, fSection());
        p.setSpacingBefore(12); p.setSpacingAfter(2);
        doc.add(p);
        doc.add(new Chunk(new LineSeparator(1.5f, 100, VIOLET, Element.ALIGN_LEFT, -2)));
    }

    private static PdfPTable table4col() throws Exception {
        PdfPTable t = new PdfPTable(4);
        t.setWidthPercentage(100);
        t.setSpacingBefore(6);
        t.setSpacingAfter(10);
        t.setWidths(new float[]{1.2f, 1.8f, 1.2f, 1.8f});
        return t;
    }

    private static void row2(PdfPTable t, String l1, String v1, String l2, String v2) {
        icell(t, l1, fLabel()); icell(t, v1, fValeur());
        icell(t, l2, fLabel()); icell(t, v2, fValeur());
    }

    private static void icell(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt != null ? txt : "", f));
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderColor(new BaseColor(220, 224, 255));
        c.setPadding(6);
        t.addCell(c);
    }

    private static void jauge(Document doc, PdfWriter writer,
                               String label, int val, int max, BaseColor col) throws Exception {
        PdfPTable t = new PdfPTable(new float[]{2f, 5f, 0.8f});
        t.setWidthPercentage(92); t.setSpacingBefore(4); t.setSpacingAfter(4);
        PdfPCell lc = new PdfPCell(new Phrase(label, fLabel()));
        lc.setBorder(Rectangle.NO_BORDER); lc.setVerticalAlignment(Element.ALIGN_MIDDLE); lc.setPadding(4);
        t.addCell(lc);
        PdfPCell bc = new PdfPCell();
        bc.setBorder(Rectangle.NO_BORDER); bc.setPadding(6);
        bc.addElement(barre(writer, max > 0 ? (float) val / max : 0, col));
        t.addCell(bc);
        PdfPCell sc = new PdfPCell(new Phrase(val + "/" + max, fBadge(col)));
        sc.setBorder(Rectangle.NO_BORDER); sc.setHorizontalAlignment(Element.ALIGN_RIGHT);
        sc.setVerticalAlignment(Element.ALIGN_MIDDLE); sc.setPadding(4);
        t.addCell(sc);
        doc.add(t);
    }

    private static Image barre(PdfWriter writer, float ratio, BaseColor col) throws Exception {
        PdfTemplate tpl = writer.getDirectContent().createTemplate(200, 12);
        tpl.setColorFill(new BaseColor(225, 228, 245));
        tpl.roundRectangle(0, 1, 200, 10, 5); tpl.fill();
        if (ratio > 0) {
            tpl.setColorFill(col);
            tpl.roundRectangle(0, 1, 200 * ratio, 10, 5); tpl.fill();
        }
        return Image.getInstance(tpl);
    }

    private static void carte(Document doc, String titre, String texte,
                               BaseColor bg, BaseColor border) throws Exception {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100); t.setSpacingBefore(6); t.setSpacingAfter(6);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(bg); cell.setBorderColor(border);
        cell.setBorderWidth(2); cell.setPadding(10);
        Paragraph p = new Paragraph();
        p.add(new Chunk(titre + "\n", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, border)));
        p.add(new Chunk(texte, fValeur()));
        cell.addElement(p); t.addCell(cell);
        doc.add(t);
    }

    private static void kpiCard(PdfPTable t, String label, String val, BaseColor col) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(col); cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(12); cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Chunk(val + "\n",  new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD,   BLANC)));
        p.add(new Chunk(label,        new Font(Font.FontFamily.HELVETICA,  8, Font.NORMAL, new BaseColor(230,235,255))));
        cell.addElement(p); t.addCell(cell);
    }

    private static void tcell(PdfPTable t, String txt, BaseColor bg, int align) {
        PdfPCell c = new PdfPCell(new Phrase(txt != null ? txt : "", fTCell()));
        c.setBackgroundColor(bg); c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(6); c.setHorizontalAlignment(align);
        t.addCell(c);
    }

    private static PdfPCell badge(String txt, BaseColor txtCol, BaseColor bg) {
        PdfPCell c = new PdfPCell(new Phrase(txt, fBadge(txtCol)));
        c.setBackgroundColor(bg); c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(6); c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private static void footer(Document doc) throws Exception {
        doc.add(Chunk.NEWLINE);
        doc.add(new Chunk(new LineSeparator(0.5f, 100, GRIS_DARK, Element.ALIGN_LEFT, 0)));
        doc.add(Chunk.NEWLINE);
        Paragraph p = new Paragraph(
            "FocusKid (c) " + LocalDate.now().getYear() +
            "  -  Document genere automatiquement  -  Confidentiel",
            new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, GRIS_DARK));
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
    }

    /* ── helpers ── */
    private static String str(Object o)         { return o != null ? o.toString() : ""; }
    private static String heure(java.sql.Time t){ return t != null ? t.toString().substring(0, 5) : ""; }
    private static boolean vide(String s)        { return s == null || s.isBlank(); }
}
