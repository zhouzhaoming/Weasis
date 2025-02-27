/*
 * Copyright (c) 2009-2020 Weasis Team and other contributors.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0, or the Apache
 * License, Version 2.0 which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package org.weasis.dicom.rt;

import java.util.Hashtable;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weasis.core.api.gui.Insertable;
import org.weasis.core.api.gui.Insertable.Type;
import org.weasis.core.api.gui.InsertableFactory;
import org.weasis.core.api.media.data.MediaSeries;
import org.weasis.dicom.viewer2d.EventManager;

/**
 * @author Tomas Skripcak
 * @author Nicolas Roduit
 */
@org.osgi.service.component.annotations.Component(
    service = InsertableFactory.class,
    property = {"org.weasis.dicom.viewer2d.View2dContainer=true"})
public class RtDisplayToolFactory implements InsertableFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(RtDisplayToolFactory.class);

  private RtDisplayTool toolPane = null;

  @Override
  public Insertable createInstance(Hashtable<String, Object> properties) {
    Object val = properties.get(MediaSeries.class.getName());
    boolean compatible = false;
    if (val instanceof MediaSeries<?> mediaSeries) {
      compatible = RtDisplayTool.isCtLinkedRT(mediaSeries);
    }
    if (!compatible) {
      return null;
    }
    if (toolPane == null) {
      toolPane = new RtDisplayTool();
      EventManager.getInstance().addSeriesViewerListener(toolPane);
    }
    return toolPane;
  }

  @Override
  public void dispose(Insertable tool) {
    if (toolPane != null) {
      EventManager.getInstance().removeSeriesViewerListener(toolPane);
      toolPane = null;
    }
  }

  @Override
  public boolean isComponentCreatedByThisFactory(Insertable component) {
    return component instanceof RtDisplayTool;
  }

  @Override
  public Type getType() {
    return Type.TOOL_EXT;
  }

  // ================================================================================
  // OSGI service implementation
  // ================================================================================

  @Activate
  protected void activate(ComponentContext context) {
    LOGGER.info("Activate the RT panel");
  }

  @Deactivate
  protected void deactivate(ComponentContext context) {
    LOGGER.info("Deactivate the RT panel");
  }
}
