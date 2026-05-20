package io.github.ffakira.rsps.client.desktop.itemicon;

final class ReferenceTriangleWalker {

  void drawTriangle(
      int yA,
      int yB,
      int yC,
      int xA,
      int xB,
      int xC,
      int valueA,
      int valueB,
      int valueC,
      int[] pixels,
      int referenceAlpha,
      SpanDrawer spanDrawer
  ) {
    int dxAB = 0;
    int dvAB = 0;
    if (yB != yA) {
      dxAB = ((xB - xA) << 16) / (yB - yA);
      dvAB = (valueB - valueA) / (yB - yA);
    }
    int dxBC = 0;
    int dvBC = 0;
    if (yC != yB) {
      dxBC = ((xC - xB) << 16) / (yC - yB);
      dvBC = (valueC - valueB) / (yC - yB);
    }
    int dxCA = 0;
    int dvCA = 0;
    if (yC != yA) {
      dxCA = ((xA - xC) << 16) / (yA - yC);
      dvCA = (valueA - valueC) / (yA - yC);
    }
    if (yA <= yB && yA <= yC) {
      if (yA >= IconRenderConstants.ICON_SIZE) {
        return;
      }
      if (yB > IconRenderConstants.ICON_SIZE) {
        yB = IconRenderConstants.ICON_SIZE;
      }
      if (yC > IconRenderConstants.ICON_SIZE) {
        yC = IconRenderConstants.ICON_SIZE;
      }
      if (yB < yC) {
        int xCA = xA << 16;
        int xAB = xA << 16;
        int valueCA = valueA;
        int valueAB = valueA;
        if (yA < 0) {
          xCA -= dxCA * yA;
          xAB -= dxAB * yA;
          valueCA -= dvCA * yA;
          valueAB -= dvAB * yA;
          yA = 0;
        }
        int xBC = xB << 16;
        int valueBC = valueB;
        if (yB < 0) {
          xBC -= dxBC * yB;
          valueBC -= dvBC * yB;
          yB = 0;
        }
        if (yA != yB && dxCA < dxAB || yA == yB && dxCA > dxBC) {
          yC -= yB;
          yB -= yA;
          int rowOffset = IconRenderConstants.ROW_OFFSETS[yA];
          for (; --yB >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
            spanDrawer.draw(pixels, rowOffset, xCA >> 16, xAB >> 16, valueCA, valueAB, referenceAlpha);
            xCA += dxCA;
            xAB += dxAB;
            valueCA += dvCA;
            valueAB += dvAB;
          }
          while (--yC >= 0) {
            spanDrawer.draw(pixels, rowOffset, xCA >> 16, xBC >> 16, valueCA, valueBC, referenceAlpha);
            xCA += dxCA;
            xBC += dxBC;
            valueCA += dvCA;
            valueBC += dvBC;
            rowOffset += IconRenderConstants.ICON_SIZE;
          }
          return;
        }
        yC -= yB;
        yB -= yA;
        int rowOffset = IconRenderConstants.ROW_OFFSETS[yA];
        for (; --yB >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
          spanDrawer.draw(pixels, rowOffset, xAB >> 16, xCA >> 16, valueAB, valueCA, referenceAlpha);
          xCA += dxCA;
          xAB += dxAB;
          valueCA += dvCA;
          valueAB += dvAB;
        }
        while (--yC >= 0) {
          spanDrawer.draw(pixels, rowOffset, xBC >> 16, xCA >> 16, valueBC, valueCA, referenceAlpha);
          xCA += dxCA;
          xBC += dxBC;
          valueCA += dvCA;
          valueBC += dvBC;
          rowOffset += IconRenderConstants.ICON_SIZE;
        }
        return;
      }
      int xBC = xA << 16;
      int xAB = xA << 16;
      int valueBC = valueA;
      int valueAB = valueA;
      if (yA < 0) {
        xBC -= dxCA * yA;
        xAB -= dxAB * yA;
        valueBC -= dvCA * yA;
        valueAB -= dvAB * yA;
        yA = 0;
      }
      int xCA = xC << 16;
      int valueCA = valueC;
      if (yC < 0) {
        xCA -= dxBC * yC;
        valueCA -= dvBC * yC;
        yC = 0;
      }
      if (yA != yC && dxCA < dxAB || yA == yC && dxBC > dxAB) {
        yB -= yC;
        yC -= yA;
        int rowOffset = IconRenderConstants.ROW_OFFSETS[yA];
        for (; --yC >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
          spanDrawer.draw(pixels, rowOffset, xBC >> 16, xAB >> 16, valueBC, valueAB, referenceAlpha);
          xBC += dxCA;
          xAB += dxAB;
          valueBC += dvCA;
          valueAB += dvAB;
        }
        while (--yB >= 0) {
          spanDrawer.draw(pixels, rowOffset, xCA >> 16, xAB >> 16, valueCA, valueAB, referenceAlpha);
          xCA += dxBC;
          xAB += dxAB;
          valueCA += dvBC;
          valueAB += dvAB;
          rowOffset += IconRenderConstants.ICON_SIZE;
        }
        return;
      }
      yB -= yC;
      yC -= yA;
      int rowOffset = IconRenderConstants.ROW_OFFSETS[yA];
      for (; --yC >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
        spanDrawer.draw(pixels, rowOffset, xAB >> 16, xBC >> 16, valueAB, valueBC, referenceAlpha);
        xBC += dxCA;
        xAB += dxAB;
        valueBC += dvCA;
        valueAB += dvAB;
      }
      while (--yB >= 0) {
        spanDrawer.draw(pixels, rowOffset, xAB >> 16, xCA >> 16, valueAB, valueCA, referenceAlpha);
        xCA += dxBC;
        xAB += dxAB;
        valueCA += dvBC;
        valueAB += dvAB;
        rowOffset += IconRenderConstants.ICON_SIZE;
      }
      return;
    }
    if (yB <= yC) {
      if (yB >= IconRenderConstants.ICON_SIZE) {
        return;
      }
      if (yC > IconRenderConstants.ICON_SIZE) {
        yC = IconRenderConstants.ICON_SIZE;
      }
      if (yA > IconRenderConstants.ICON_SIZE) {
        yA = IconRenderConstants.ICON_SIZE;
      }
      if (yC < yA) {
        int xAB = xB << 16;
        int xBC = xB << 16;
        int valueAB = valueB;
        int valueBC = valueB;
        if (yB < 0) {
          xAB -= dxAB * yB;
          xBC -= dxBC * yB;
          valueAB -= dvAB * yB;
          valueBC -= dvBC * yB;
          yB = 0;
        }
        int xCA = xC << 16;
        int valueCA = valueC;
        if (yC < 0) {
          xCA -= dxCA * yC;
          valueCA -= dvCA * yC;
          yC = 0;
        }
        if (yB != yC && dxAB < dxBC || yB == yC && dxAB > dxCA) {
          yA -= yC;
          yC -= yB;
          int rowOffset = IconRenderConstants.ROW_OFFSETS[yB];
          for (; --yC >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
            spanDrawer.draw(pixels, rowOffset, xAB >> 16, xBC >> 16, valueAB, valueBC, referenceAlpha);
            xAB += dxAB;
            xBC += dxBC;
            valueAB += dvAB;
            valueBC += dvBC;
          }
          while (--yA >= 0) {
            spanDrawer.draw(pixels, rowOffset, xAB >> 16, xCA >> 16, valueAB, valueCA, referenceAlpha);
            xAB += dxAB;
            xCA += dxCA;
            valueAB += dvAB;
            valueCA += dvCA;
            rowOffset += IconRenderConstants.ICON_SIZE;
          }
          return;
        }
        yA -= yC;
        yC -= yB;
        int rowOffset = IconRenderConstants.ROW_OFFSETS[yB];
        for (; --yC >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
          spanDrawer.draw(pixels, rowOffset, xBC >> 16, xAB >> 16, valueBC, valueAB, referenceAlpha);
          xAB += dxAB;
          xBC += dxBC;
          valueAB += dvAB;
          valueBC += dvBC;
        }
        while (--yA >= 0) {
          spanDrawer.draw(pixels, rowOffset, xCA >> 16, xAB >> 16, valueCA, valueAB, referenceAlpha);
          xAB += dxAB;
          xCA += dxCA;
          valueAB += dvAB;
          valueCA += dvCA;
          rowOffset += IconRenderConstants.ICON_SIZE;
        }
        return;
      }
      int xCA = xB << 16;
      int xBC = xB << 16;
      int valueCA = valueB;
      int valueBC = valueB;
      if (yB < 0) {
        xCA -= dxAB * yB;
        xBC -= dxBC * yB;
        valueCA -= dvAB * yB;
        valueBC -= dvBC * yB;
        yB = 0;
      }
      int xAB = xA << 16;
      int valueAB = valueA;
      if (yA < 0) {
        xAB -= dxCA * yA;
        valueAB -= dvCA * yA;
        yA = 0;
      }
      if (dxAB < dxBC) {
        yC -= yA;
        yA -= yB;
        int rowOffset = IconRenderConstants.ROW_OFFSETS[yB];
        for (; --yA >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
          spanDrawer.draw(pixels, rowOffset, xCA >> 16, xBC >> 16, valueCA, valueBC, referenceAlpha);
          xCA += dxAB;
          xBC += dxBC;
          valueCA += dvAB;
          valueBC += dvBC;
        }
        while (--yC >= 0) {
          spanDrawer.draw(pixels, rowOffset, xAB >> 16, xBC >> 16, valueAB, valueBC, referenceAlpha);
          xAB += dxCA;
          xBC += dxBC;
          valueAB += dvCA;
          valueBC += dvBC;
          rowOffset += IconRenderConstants.ICON_SIZE;
        }
        return;
      }
      yC -= yA;
      yA -= yB;
      int rowOffset = IconRenderConstants.ROW_OFFSETS[yB];
      for (; --yA >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
        spanDrawer.draw(pixels, rowOffset, xBC >> 16, xCA >> 16, valueBC, valueCA, referenceAlpha);
        xCA += dxAB;
        xBC += dxBC;
        valueCA += dvAB;
        valueBC += dvBC;
      }
      while (--yC >= 0) {
        spanDrawer.draw(pixels, rowOffset, xBC >> 16, xAB >> 16, valueBC, valueAB, referenceAlpha);
        xAB += dxCA;
        xBC += dxBC;
        valueAB += dvCA;
        valueBC += dvBC;
        rowOffset += IconRenderConstants.ICON_SIZE;
      }
      return;
    }
    if (yC >= IconRenderConstants.ICON_SIZE) {
      return;
    }
    if (yA > IconRenderConstants.ICON_SIZE) {
      yA = IconRenderConstants.ICON_SIZE;
    }
    if (yB > IconRenderConstants.ICON_SIZE) {
      yB = IconRenderConstants.ICON_SIZE;
    }
    if (yA < yB) {
      int xBC = xC << 16;
      int xCA = xC << 16;
      int valueBC = valueC;
      int valueCA = valueC;
      if (yC < 0) {
        xBC -= dxBC * yC;
        xCA -= dxCA * yC;
        valueBC -= dvBC * yC;
        valueCA -= dvCA * yC;
        yC = 0;
      }
      int xAB = xA << 16;
      int valueAB = valueA;
      if (yA < 0) {
        xAB -= dxAB * yA;
        valueAB -= dvAB * yA;
        yA = 0;
      }
      if (dxBC < dxCA) {
        yB -= yA;
        yA -= yC;
        int rowOffset = IconRenderConstants.ROW_OFFSETS[yC];
        for (; --yA >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
          spanDrawer.draw(pixels, rowOffset, xBC >> 16, xCA >> 16, valueBC, valueCA, referenceAlpha);
          xBC += dxBC;
          xCA += dxCA;
          valueBC += dvBC;
          valueCA += dvCA;
        }
        while (--yB >= 0) {
          spanDrawer.draw(pixels, rowOffset, xBC >> 16, xAB >> 16, valueBC, valueAB, referenceAlpha);
          xBC += dxBC;
          xAB += dxAB;
          valueBC += dvBC;
          valueAB += dvAB;
          rowOffset += IconRenderConstants.ICON_SIZE;
        }
        return;
      }
      yB -= yA;
      yA -= yC;
      int rowOffset = IconRenderConstants.ROW_OFFSETS[yC];
      for (; --yA >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
        spanDrawer.draw(pixels, rowOffset, xCA >> 16, xBC >> 16, valueCA, valueBC, referenceAlpha);
        xBC += dxBC;
        xCA += dxCA;
        valueBC += dvBC;
        valueCA += dvCA;
      }
      while (--yB >= 0) {
        spanDrawer.draw(pixels, rowOffset, xAB >> 16, xBC >> 16, valueAB, valueBC, referenceAlpha);
        xBC += dxBC;
        xAB += dxAB;
        valueBC += dvBC;
        valueAB += dvAB;
        rowOffset += IconRenderConstants.ICON_SIZE;
      }
      return;
    }
    int xAB = xC << 16;
    int xCA = xC << 16;
    int valueAB = valueC;
    int valueCA = valueC;
    if (yC < 0) {
      xAB -= dxBC * yC;
      xCA -= dxCA * yC;
      valueAB -= dvBC * yC;
      valueCA -= dvCA * yC;
      yC = 0;
    }
    int xBC = xB << 16;
    int valueBC = valueB;
    if (yB < 0) {
      xBC -= dxAB * yB;
      valueBC -= dvAB * yB;
      yB = 0;
    }
    if (dxBC < dxCA) {
      yA -= yB;
      yB -= yC;
      int rowOffset = IconRenderConstants.ROW_OFFSETS[yC];
      for (; --yB >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
        spanDrawer.draw(pixels, rowOffset, xAB >> 16, xCA >> 16, valueAB, valueCA, referenceAlpha);
        xAB += dxBC;
        xCA += dxCA;
        valueAB += dvBC;
        valueCA += dvCA;
      }
      while (--yA >= 0) {
        spanDrawer.draw(pixels, rowOffset, xBC >> 16, xCA >> 16, valueBC, valueCA, referenceAlpha);
        xBC += dxAB;
        xCA += dxCA;
        valueBC += dvAB;
        valueCA += dvCA;
        rowOffset += IconRenderConstants.ICON_SIZE;
      }
      return;
    }
    yA -= yB;
    yB -= yC;
    int rowOffset = IconRenderConstants.ROW_OFFSETS[yC];
    for (; --yB >= 0; rowOffset += IconRenderConstants.ICON_SIZE) {
      spanDrawer.draw(pixels, rowOffset, xCA >> 16, xAB >> 16, valueCA, valueAB, referenceAlpha);
      xAB += dxBC;
      xCA += dxCA;
      valueAB += dvBC;
      valueCA += dvCA;
    }
    while (--yA >= 0) {
      spanDrawer.draw(pixels, rowOffset, xCA >> 16, xBC >> 16, valueCA, valueBC, referenceAlpha);
      xBC += dxAB;
      xCA += dxCA;
      valueBC += dvAB;
      valueCA += dvCA;
      rowOffset += IconRenderConstants.ICON_SIZE;
    }
  }

  @FunctionalInterface
  interface SpanDrawer {

    void draw(int[] pixels, int rowOffset, int startX, int endX, int startValue, int endValue, int referenceAlpha);
  }
}
