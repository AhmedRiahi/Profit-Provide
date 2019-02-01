package com.pp.engine.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pp.framework.kafka.KafkaTopics;
import com.pp.framework.kafka.sender.PPSender;

@Component
public class CleanerJob implements Job{

	private static final Logger log = LoggerFactory.getLogger(CleanerJob.class);
	
	@Autowired
	private PPSender sender;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Executing CleanerJob.");
		this.sender.send(KafkaTopics.Cleaner.CLEAN+KafkaTopics.IN, "");
	}

}