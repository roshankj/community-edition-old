/*
 * #%L
 * Alfresco Repository
 * %%
 * Copyright (C) 2005 - 2016 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.alfresco.repo.attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.alfresco.util.ApplicationContextHelper;
import org.alfresco.util.CronTriggerBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.JobDetail;
import org.springframework.context.ApplicationContext;

/**
 * Integration tests for the {@link PropTablesCleanupJob} class.
 * 
 * @author Matt Ward
 */
public class PropTablesCleanupJobIntegrationTest
{
    private static ApplicationContext ctx;
    private CronTriggerBean jobTrigger;
    
    @BeforeClass
    public static void setUpClass()
    {
        ctx = ApplicationContextHelper.getApplicationContext();
    }
    
    @Before
    public void setUp() throws Exception
    {
        jobTrigger = ctx.getBean("propTablesCleanupTrigger", CronTriggerBean.class);
    }
    
    @Test
    public void checkJobDetails()
    {
        JobDetail jobDetail = jobTrigger.getJobDetail();
        assertEquals(PropTablesCleanupJob.class, jobDetail.getJobClass());
        assertTrue("JobDetail did not contain PropTablesCleaner reference",
                    jobDetail.getJobDataMap().get("propTablesCleaner") instanceof PropTablesCleaner);
    }
}
