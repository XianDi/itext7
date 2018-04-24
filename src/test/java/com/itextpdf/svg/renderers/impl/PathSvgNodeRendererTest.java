/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2018 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.svg.renderers.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.ITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;

import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.styledxmlparser.node.IElementNode;
import com.itextpdf.styledxmlparser.node.impl.jsoup.JsoupXmlParser;
import com.itextpdf.svg.processors.impl.DefaultSvgProcessor;
import com.itextpdf.svg.renderers.ISvgNodeRenderer;
import com.itextpdf.svg.renderers.SvgDrawContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Category(IntegrationTest.class)
public class PathSvgNodeRendererTest {

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/svg/renderers/impl/PathSvgNodeRendererTest/";
    public static final String destinationFolder = "./target/test/com/itextpdf/svg/renderers/impl/PathSvgNodeRendererTest/";

    @BeforeClass
    public static void beforeClass() {
        ITextTest.createDestinationFolder(destinationFolder);
    }

    @Test
    public void pathLineRendererMoveToTest() throws IOException, InterruptedException {
        String filename = "pathNodeRendererMoveToTest.pdf";
        PdfDocument doc = new PdfDocument(new PdfWriter(destinationFolder + filename));
        doc.addNewPage();

        Map<String, String> pathShapes = new HashMap<String, String>();
        pathShapes.put( "d", "M 100,100, L300,100,L200,300,z" );


        ISvgNodeRenderer pathRenderer = new PathSvgNodeRenderer();
        pathRenderer.setAttributesAndStyles( pathShapes );

        SvgDrawContext context = new SvgDrawContext();
        PdfCanvas cv = new PdfCanvas(doc, 1);
        context.pushCanvas(cv);
        pathRenderer.draw( context );
        doc.close();
        Assert.assertNull( new CompareTool().compareByContent( destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_" ) );

    }

    @Test
    public void pathLineRendererMoveToTest1() throws IOException, InterruptedException {
        String filename = "pathNodeRendererMoveToTest1.pdf";
        PdfDocument doc = new PdfDocument( new PdfWriter( destinationFolder + filename ) );
        doc.addNewPage();

        Map<String, String> pathShapes = new HashMap<String, String>();
        pathShapes.put( "d", "M 100 100 l300 100 L200 300 z" );

        ISvgNodeRenderer pathRenderer = new PathSvgNodeRenderer();
        pathRenderer.setAttributesAndStyles( pathShapes );

        SvgDrawContext context = new SvgDrawContext();
        PdfCanvas cv = new PdfCanvas( doc, 1 );
        context.pushCanvas( cv );
        pathRenderer.draw( context );
        doc.close();
        Assert.assertNull( new CompareTool().compareByContent( destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_" ) );

    }

    @Test
    @Ignore("RND-900")
    public void pathLineRendererCurveToTest() throws IOException, InterruptedException {
        String filename = "pathNodeRendererCurveToTest.pdf";
        PdfDocument doc = new PdfDocument( new PdfWriter( destinationFolder + filename ) );
        doc.addNewPage();

        Map<String, String> pathShapes = new HashMap<String, String>();
        pathShapes.put( "d", "M100,200 C100,100 250,100 250,200 S400,300 400,200,z" );


        ISvgNodeRenderer pathRenderer = new PathSvgNodeRenderer();
        pathRenderer.setAttributesAndStyles( pathShapes );

        SvgDrawContext context = new SvgDrawContext();
        PdfCanvas cv = new PdfCanvas( doc, 1 );
        context.pushCanvas( cv );
        pathRenderer.draw( context );
        doc.close();
        Assert.assertNull( new CompareTool().compareByContent( destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_" ) );

    }

    @Test
    @Ignore("RND-900")
    public void pathLineRendererCurveToTest1() throws IOException, InterruptedException {
        String filename = "pathNodeRendererCurveToTest1.pdf";
        PdfDocument doc = new PdfDocument( new PdfWriter( destinationFolder + filename ) );
        doc.addNewPage();

        Map<String, String> pathShapes = new HashMap<String, String>();
        pathShapes.put( "d", "M100 200 C100 100 250 100 250 200 S400 300 400 200 z" );

        ISvgNodeRenderer pathRenderer = new PathSvgNodeRenderer();
        pathRenderer.setAttributesAndStyles( pathShapes );

        SvgDrawContext context = new SvgDrawContext();
        PdfCanvas cv = new PdfCanvas( doc, 1 );
        context.pushCanvas( cv );
        pathRenderer.draw( context );
        doc.close();
        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));

    }

    @Test
    public void  pathNodeRendererQCurveToCurveToTest()throws IOException, InterruptedException{
        String filename = "pathNodeRendererQCurveToCurveToTest.pdf";
        PdfDocument doc = new PdfDocument( new PdfWriter( destinationFolder + filename ) );
        doc.addNewPage();

        Map<String, String> pathShapes = new HashMap<String, String>();
        pathShapes.put( "d", "M200,300 Q400,50 600,300,z" );

        ISvgNodeRenderer pathRenderer = new PathSvgNodeRenderer();
        pathRenderer.setAttributesAndStyles( pathShapes );

        SvgDrawContext context = new SvgDrawContext();
        PdfCanvas cv = new PdfCanvas( doc, 1 );
        context.pushCanvas( cv );
        pathRenderer.draw( context );
        doc.close();
        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void  pathNodeRendererQCurveToCurveToTest1()throws IOException, InterruptedException{
        String filename = "pathNodeRendererQCurveToCurveToTest1.pdf";
        PdfDocument doc = new PdfDocument( new PdfWriter( destinationFolder + filename ) );
        doc.addNewPage();

        Map<String, String> pathShapes = new HashMap<String, String>();
        pathShapes.put( "d", "M200 300 Q400 50 600 300 z" );

        ISvgNodeRenderer pathRenderer = new PathSvgNodeRenderer();
        pathRenderer.setAttributesAndStyles( pathShapes );

        SvgDrawContext context = new SvgDrawContext();
        PdfCanvas cv = new PdfCanvas( doc, 1 );
        context.pushCanvas( cv );
        pathRenderer.draw( context );
        doc.close();
        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }
    @Test
    public void pathNodeRederarIntegrationTest() throws IOException, InterruptedException{
        String filename = "pathNodeRederarIntegrationTest.pdf";
        PdfDocument doc = new PdfDocument(new PdfWriter(destinationFolder + filename));
        doc.addNewPage();

        String svgFilename = "pathRendererTest.svg";
        InputStream xmlStream = new FileInputStream( sourceFolder + svgFilename );
        IElementNode rootTag = new JsoupXmlParser().parse( xmlStream, "ISO-8859-1" );

        DefaultSvgProcessor processor = new DefaultSvgProcessor();
        ISvgNodeRenderer root = processor.process( rootTag );

        SvgDrawContext context = new SvgDrawContext();
        PdfCanvas cv = new PdfCanvas(doc, 1);
        context.pushCanvas(cv);
       Assert.assertTrue(  root.getChildren().get( 0 )instanceof PathSvgNodeRenderer );
        root.getChildren().get( 0 ).draw( context );
       // root.getChildren().get( 0 ).draw( context );
        doc.close();
    }
}