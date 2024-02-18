//package com.remote.submission.service.Consumer;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//import com.remote.submission.config.Constants;
//import com.remote.submission.model.FileSubmission;
//
//@Service
//public class ConsumerService {
//
//    @RabbitListener(queues = Constants.FILE_EXECUTION_QUEUE)
//    public void receiveFileExecution(FileSubmission fileSubmission){
//        System.out.println("Received file execution: " + fileSubmission);
//    }
//}
