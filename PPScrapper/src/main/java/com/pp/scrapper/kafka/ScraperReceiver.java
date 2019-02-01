package com.pp.scrapper.kafka;

import com.pp.database.dao.mozart.DescriptorWorkflowDataPackageDAO;
import com.pp.database.model.mozart.DescriptorWorkflowDataPackage;
import com.pp.framework.kafka.KafkaTopics;
import com.pp.framework.kafka.sender.PPSender;
import com.pp.scrapper.service.ScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ScraperReceiver {
	
	private static final Logger log = LoggerFactory.getLogger(ScraperReceiver.class);
	
	@Autowired
	private PPSender sender;
	@Autowired
	private DescriptorWorkflowDataPackageDAO dwdpDAO;
	@Autowired
	private ScraperService scraperService;
	
	
	@KafkaListener(topics = KafkaTopics.Scraper.MATCH_DESCRIPTOR+KafkaTopics.IN)
	public void matchDescriptor(String workflowId) {
        DescriptorWorkflowDataPackage dwdp = null;
		try {
			log.info("Scraper received message='{}'"+ workflowId);
			dwdp = this.dwdpDAO.get(workflowId);
			this.scraperService.scrapDescriptor(dwdp);
			log.info("Saving {} scrapedContents",dwdp.getScrapedContents().size());
			this.dwdpDAO.updateCollection(dwdp,"individuals",dwdp.getIndividuals());
			this.sender.send(KafkaTopics.Scraper.MATCH_DESCRIPTOR+KafkaTopics.OUT,workflowId);
		}catch(Exception e) {
			log.error("Scraper Exception",e);
			if(dwdp != null){
				dwdp.getDebugInformation().setException(e.getMessage()+"\n"+ Arrays.stream(e.getStackTrace()).map(st -> st.toString()).collect(Collectors.joining("\n")));
				this.dwdpDAO.save(dwdp);
			}else{
				log.error("Enable to set debug information exception because dwdp is null");
			}
			this.sender.send(KafkaTopics.Scraper.MATCH_DESCRIPTOR+KafkaTopics.ERROR, workflowId);
		}
	}
}