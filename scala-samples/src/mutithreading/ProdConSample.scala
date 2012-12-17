package mutithreading

import concurrent.{MailBox, ops}

  object ProdConSample
  {
    class Producer(drop : Drop)
      extends Runnable
    {
      val importantInfo : Array[String] = Array(
        "Message test 1",
        "Message test 2",
        "Message test 3",
        "Message test 4"
      );
    
      override def run() : Unit =
      {
        importantInfo.foreach((msg) => drop.put(msg))
        drop.put("DONE")
      }
    }
    
    class Consumer(drop : Drop)
      extends Runnable
    {
      override def run() : Unit =
      {
        var message = drop.take()
        while (message != "DONE")
        {
          System.out.format("MESSAGE RECEIVED: %s%n", message)
          message = drop.take()
        }
      }
    }

    class Drop
    {
      private val m = new MailBox()
      
      private case class Empty()
      private case class Full(x : String)
      
      m send Empty()  // initialization
      
      def put(msg : String) : Unit =
      {
        m receive
        {
          case Empty() =>
            m send Full(msg)
        }
      }
      
      def take() : String =
      {
        m receive
        {
          case Full(msg) =>
            m send Empty(); msg
        }
      }
    }
  
    def main(args : Array[String]) : Unit =
    {
      // Create Drop
      val drop = new Drop()
      
      // Spawn Producer
      new Thread(new Producer(drop)).start();
      
      // Spawn Consumer
      new Thread(new Consumer(drop)).start();
    }
  }
