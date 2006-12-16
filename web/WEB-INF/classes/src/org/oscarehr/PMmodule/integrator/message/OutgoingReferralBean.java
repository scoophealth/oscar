package org.oscarehr.PMmodule.integrator.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.oscarehr.PMmodule.model.ClientReferral;
import org.springframework.jms.core.MessageCreator;

public class OutgoingReferralBean implements MessageCreator {

        private ClientReferral referral;

        public void setReferral(ClientReferral referral) {
                this.referral = referral;
        }

        public Message createMessage(Session session) throws JMSException {
                 ObjectMessage message = session.createObjectMessage();
                 message.setObject(referral);
                 return message;
        }

}

