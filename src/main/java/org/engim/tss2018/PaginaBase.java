package org.engim.tss2018;

import org.apache.wicket.markup.html.WebPage;

/**
 *
 * @author svilupposw
 */
public class PaginaBase extends WebPage
{
  public PaginaBase()
  {
    add(new NavMenu("navmenu"));
  }
}
