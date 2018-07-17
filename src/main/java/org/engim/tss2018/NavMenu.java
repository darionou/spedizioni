package org.engim.tss2018;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

public class NavMenu extends Panel
{
  public NavMenu(String id)
  {
    super(id);

    BookmarkablePageLink home =
      new BookmarkablePageLink("home", 
                          HomePage.class);
    add(home);

    BookmarkablePageLink merci =
      new BookmarkablePageLink("merci", 
                          PaginaMerci.class);
    add(merci);

    BookmarkablePageLink sped =
      new BookmarkablePageLink("spedizioni", 
                          PaginaSpedizioni.class);
    add(sped);

    BookmarkablePageLink cmt =
      new BookmarkablePageLink("cmt", 
                          PaginaCMT.class);
    add(cmt);
  }
  
}
