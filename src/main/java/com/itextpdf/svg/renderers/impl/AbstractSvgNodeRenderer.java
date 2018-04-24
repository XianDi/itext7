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

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.styledxmlparser.css.util.CssUtils;
import com.itextpdf.svg.SvgTagConstants;
import com.itextpdf.svg.renderers.ISvgNodeRenderer;
import com.itextpdf.svg.renderers.SvgDrawContext;
import com.itextpdf.svg.utils.TransformUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 */
public abstract class AbstractSvgNodeRenderer implements ISvgNodeRenderer {

    private ISvgNodeRenderer parent;
    private final List<ISvgNodeRenderer> children = new ArrayList<>();
    protected Map<String, String> attributesAndStyles;
    private boolean doFill = false;

    @Override
    public void setParent(ISvgNodeRenderer parent) {
        this.parent = parent;
    }

    @Override
    public ISvgNodeRenderer getParent() {
        return parent;
    }

    @Override
    public final void addChild(ISvgNodeRenderer child) {
        // final method, in order to disallow adding null
        if (child != null) {
            children.add(child);
        }
    }

    @Override
    public final List<ISvgNodeRenderer> getChildren() {
        // final method, in order to disallow modifying the List
        return Collections.unmodifiableList(children);
    }

    @Override
    public void setAttributesAndStyles(Map<String, String> attributesAndStyles) {
        this.attributesAndStyles = attributesAndStyles;
    }

    /**
     * Applies transformations set to this object, if any, and delegates the drawing of this element and its children
     * to the {@link #doDraw(SvgDrawContext) doDraw} method.
     *
     * @param context the object that knows the place to draw this element and maintains its state
     */
    @Override
    public final void draw(SvgDrawContext context) {
        PdfCanvas currentCanvas = context.getCurrentCanvas();

        if (this.attributesAndStyles != null) {
            String transformString = this.attributesAndStyles.get(SvgTagConstants.TRANSFORM);

            if (transformString != null && !transformString.isEmpty()) {
                AffineTransform transformation = TransformUtils.parseTransform(transformString);
                currentCanvas.concatMatrix(transformation);
            }
        }

        preDraw(context);
        doDraw(context);
        postDraw(context);

        if (attributesAndStyles != null && attributesAndStyles.containsKey(SvgTagConstants.ID)) {
            context.addNamedObject(attributesAndStyles.get(SvgTagConstants.ID), this);
        }
    }


    /**
     * Operations to perform before drawing an element.
     * This includes setting stroke color and width, fill color.
     *
     * @param context the svg draw context
     */
    void preDraw(SvgDrawContext context) {
        if (this.attributesAndStyles != null) {
            PdfCanvas currentCanvas = context.getCurrentCanvas();

            // fill
            {
                String fillRawValue = getAttribute(SvgTagConstants.FILL);

                this.doFill = !SvgTagConstants.NONE.equalsIgnoreCase(fillRawValue);

                if (doFill && canElementFill()) {
                    // todo RND-865 default style sheets
                    Color color = ColorConstants.BLACK;

                    if (fillRawValue != null) {
                        color = WebColors.getRGBColor(fillRawValue);
                    }

                    currentCanvas.setFillColor(color);
                }
            }

            // stroke
            {
                String strokeRawValue = getAttribute(SvgTagConstants.STROKE);
                DeviceRgb rgbColor = WebColors.getRGBColor(strokeRawValue);

                if (strokeRawValue != null && rgbColor != null) {
                    currentCanvas.setStrokeColor(rgbColor);

                    String strokeWidthRawValue = getAttribute(SvgTagConstants.STROKE_WIDTH);

                    float strokeWidth = 1f;

                    if ( strokeWidthRawValue != null ) {
                        strokeWidth = CssUtils.parseAbsoluteLength(strokeWidthRawValue);
                    }

                    currentCanvas.setLineWidth(strokeWidth);
                }
            }
        }
    }

    protected boolean canElementFill() {
        return true;
    }

    /**
     * Operations to be performed after drawing the element.
     * This includes filling, stroking.
     *
     * @param context the svg draw context
     */
    void postDraw(SvgDrawContext context) {
        if (this.attributesAndStyles != null) {
            PdfCanvas currentCanvas = context.getCurrentCanvas();

            // fill-rule
            if ( doFill && canElementFill() ) {
                String fillRuleRawValue = getAttribute(SvgTagConstants.FILL_RULE);

                if (SvgTagConstants.FILL_RULE_EVEN_ODD.equalsIgnoreCase(fillRuleRawValue)) {
                    // TODO RND-878
                    currentCanvas.eoFill();
                } else {
                    currentCanvas.fill();
                }
            }

            if (getAttribute(SvgTagConstants.STROKE) != null) {
                currentCanvas.stroke();
            }

            currentCanvas.closePath();
        }
    }

    /**
     * Draws this element to a canvas-like object maintained in the context.
     *
     * @param context the object that knows the place to draw this element and maintains its state
     */
    protected abstract void doDraw(SvgDrawContext context);

    @Override
    public String getAttribute(String key) {
        return attributesAndStyles.get(key);
    }

    @Override
    public void setAttribute(String key, String value) {
        this.attributesAndStyles.put(key, value);
    }
}