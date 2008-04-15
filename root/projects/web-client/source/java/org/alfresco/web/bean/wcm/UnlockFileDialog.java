/*
 * Copyright (C) 2005-2007 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing"
 */
package org.alfresco.web.bean.wcm;

import java.text.MessageFormat;

import javax.faces.context.FacesContext;

import org.alfresco.service.cmr.avm.AVMService;
import org.alfresco.service.cmr.avm.locking.AVMLockingService;
import org.alfresco.web.app.Application;
import org.alfresco.web.bean.dialog.BaseDialogBean;
import org.alfresco.web.bean.repository.Repository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bean implementation for the AVM "Unlock File" dialog
 * 
 * @author Gavin Cornwell
 */
public class UnlockFileDialog extends BaseDialogBean
{
   private static final long serialVersionUID = -2985826502913718770L;

   private static final Log logger = LogFactory.getLog(UnlockFileDialog.class);
   
   protected AVMService avmService;
   protected AVMLockingService lockingService;
   protected AVMBrowseBean avmBrowseBean;
 
   /**
    * @param avmBrowseBean The avmBrowseBean to set.
    */
   public void setAvmBrowseBean(AVMBrowseBean avmBrowseBean)
   {
      this.avmBrowseBean = avmBrowseBean;
   }

   /**
    * @param avmService    The avmService to set.
    */
   public void setAvmService(AVMService avmService)
   {
      this.avmService = avmService;
   }
   
   protected AVMService getAvmService()
   {
      if (this.avmService == null)
      {
         this.avmService = Repository.getServiceRegistry(FacesContext.getCurrentInstance()).getAVMService();
      }
      
      return this.avmService;
   }
   
   /**
    * @param lockingService The avmLockingService to set.
    */
   public void setAvmLockingService(AVMLockingService lockingService)
   {
      this.lockingService = lockingService;
   }
   
   protected AVMLockingService getAvmLockingService()
   {
      if (this.lockingService == null)
      {
         this.lockingService = Repository.getServiceRegistry(FacesContext.getCurrentInstance()).getAVMLockingService();
      }
      
      return this.lockingService;
   }
   
   // ------------------------------------------------------------------------------
   // Dialog implementation
   
   @Override
   protected String finishImpl(final FacesContext context, final String outcome)
      throws Exception
   {
      // get the content to delete
      final AVMNode node = this.avmBrowseBean.getAvmActionNode();
      if (node == null)
      {
         logger.warn("WARNING: unlock called without a current AVM Node!");
      }
      else
      {
         if (logger.isDebugEnabled())
            logger.debug("Trying to unlock AVM node: " + node.getPath());
         
         this.getAvmLockingService().removeLock(this.avmBrowseBean.getWebProject().getStoreId(), 
                  AVMUtil.getStoreRelativePath(node.getPath()));
      }
      
      return outcome;
   }

   @Override
   public boolean getFinishButtonDisabled()
   {
      return false;
   }
   
   // ------------------------------------------------------------------------------
   // Bean Getters and Setters

   /**
    * Returns the confirmation to display to the user before deleting the content.
    * 
    * @return The formatted message to display
    */
   public String getConfirmMessage()
   {
      String unlockConfirmMsg = Application.getMessage(FacesContext.getCurrentInstance(), 
         "unlock_file_confirm");

      return MessageFormat.format(unlockConfirmMsg, 
               new Object[] {this.avmBrowseBean.getAvmActionNode().getName()});
   }
}
