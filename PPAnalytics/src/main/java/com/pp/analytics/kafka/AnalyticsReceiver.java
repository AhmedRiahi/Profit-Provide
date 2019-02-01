package com.pp.analytics.kafka;

import com.pp.database.dao.mozart.DescriptorWorkflowDataPackageDAO;
import com.pp.database.model.mozart.DescriptorWorkflowDataPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.pp.analytics.service.AnalyticsService;
import com.pp.framework.kafka.KafkaTopics;
import com.pp.framework.kafka.sender.PPSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AnalyticsReceiver {

	private static final Logger log = LoggerFactory.getLogger(AnalyticsReceiver.class);

	@Autowired
	private DescriptorWorkflowDataPackageDAO dwdpDAO;
	@Autowired
	private AnalyticsService analyticsService;
	@Autowired
	private PPSender sender;

	
	@KafkaListener(topics = KafkaTopics.Analytics.ANALYSE_STANDALONE_DESCRITOR_POPULATION+KafkaTopics.IN)
	public void analyseStandaloneDescriptorPopulation(String workflowId){
		try {
			log.info("Analytics:{} received message = {}",KafkaTopics.Analytics.ANALYSE_STANDALONE_DESCRITOR_POPULATION,workflowId);
			this.analyticsService.processStandaloneDescriptorPopulation(workflowId);
			this.sender.send(KafkaTopics.Analytics.ANALYSE_STANDALONE_DESCRITOR_POPULATION+KafkaTopics.OUT, workflowId);
		}catch(Throwable e) {
			log.error(e.toString(),e);
			this.sender.send(KafkaTopics.Analytics.ANALYSE_STANDALONE_DESCRITOR_POPULATION+KafkaTopics.ERROR, workflowId);
		}
	}

	@KafkaListener(topics = KafkaTopics.Analytics.ANALYSE_JOINED_DESCRITOR_POPULATION+KafkaTopics.IN)
	public void analyseJoinedDescriptorPopulation(String workflowId){
		DescriptorWorkflowDataPackage dwdp = null;
		try {
			dwdp = this.dwdpDAO.get(workflowId);
			log.info("Analytics:{} received message = {}",KafkaTopics.Analytics.ANALYSE_JOINED_DESCRITOR_POPULATION,workflowId);
			this.analyticsService.processJoinedDescriptorPopulation(workflowId);
			this.sender.send(KafkaTopics.Analytics.ANALYSE_JOINED_DESCRITOR_POPULATION+KafkaTopics.OUT, workflowId);
		}catch(Throwable e) {
			log.error(e.toString(),e);
			if(dwdp != null){
				dwdp.getDebugInformation().setException(e.getMessage()+"\n"+ Arrays.stream(e.getStackTrace()).map(st -> st.toString()).collect(Collectors.joining("\n")));
				this.dwdpDAO.save(dwdp);
			}else{
				log.error("Enable to set debug information exception because dwdp is null");
			}
			this.sender.send(KafkaTopics.Analytics.ANALYSE_JOINED_DESCRITOR_POPULATION+KafkaTopics.ERROR, workflowId);
		}
	}


    @KafkaListener(topics = KafkaTopics.Analytics.ANALYSE_JOINER_DESCRITOR_POPULATION+KafkaTopics.IN)
    public void analyseJoinerDescriptorPopulation(String workflowId){
        DescriptorWorkflowDataPackage dwdp = null;
        try {
            dwdp = this.dwdpDAO.get(workflowId);
            log.info("Analytics:{} received message = {}",KafkaTopics.Analytics.ANALYSE_JOINER_DESCRITOR_POPULATION,workflowId);
            this.analyticsService.processJoinerDescriptorPopulation(workflowId);
            this.sender.send(KafkaTopics.Analytics.ANALYSE_JOINER_DESCRITOR_POPULATION+KafkaTopics.OUT, workflowId);
        }catch(Throwable e) {
            log.error(e.toString(),e);
            if(dwdp != null){
                dwdp.getDebugInformation().setException(e.getMessage()+"\n"+ Arrays.stream(e.getStackTrace()).map(st -> st.toString()).collect(Collectors.joining("\n")));
                this.dwdpDAO.save(dwdp);
            }else{
                log.error("Enable to set debug information exception because dwdp is null");
            }
            this.sender.send(KafkaTopics.Analytics.ANALYSE_JOINER_DESCRITOR_POPULATION+KafkaTopics.ERROR, workflowId);
        }
    }


	@KafkaListener(topics = KafkaTopics.Analytics.ANALYSE_INDIVIDUAL+KafkaTopics.IN)
	public void analyseIndividual(String individualId){
		try {
			log.info("Analytics:{} received message = {}",KafkaTopics.Analytics.ANALYSE_INDIVIDUAL,individualId);
			this.analyticsService.processIndividual(individualId);
			this.sender.send(KafkaTopics.Analytics.ANALYSE_INDIVIDUAL+KafkaTopics.OUT, individualId);
		}catch(Throwable e) {
			log.error(e.toString(),e);
			this.sender.send(KafkaTopics.Analytics.ANALYSE_INDIVIDUAL+KafkaTopics.ERROR,individualId);
		}
		
	}
	
}