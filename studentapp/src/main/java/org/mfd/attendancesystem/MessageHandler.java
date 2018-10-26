package org.mfd.attendancesystem;

import org.mfd.was.core.Message;

/**
 * Created by mfd on 21/4/18.
 */

interface MessageHandler {

	Message handle(Message message);
}
