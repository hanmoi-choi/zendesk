package zendesk

object MessageFactory {

  val welcomeMessage: String =
    """
      |__      __   _
      |\ \    / /__| |__ ___ _ __  ___
      | \ \/\/ / -_) / _/ _ \ '  \/ -_)
      |  \_/\_/\___|_\__\___/_|_|_\___|
      |
      | ____            _        _     ___                  _        _
      ||_  /___ _ _  __| |___ __| |__ / __| ___ __ _ _ _ __| |_     /_\  _ __ _ __
      | / // -_) ' \/ _` / -_|_-< / / \__ \/ -_) _` | '_/ _| ' \   / _ \| '_ \ '_ \
      |/___\___|_||_\__,_\___/__/_\_\ |___/\___\__,_|_| \__|_||_| /_/ \_\ .__/ .__/
      |                                                                 |_|  |_|
      |""".stripMargin

  val helpMessage: String =
    """
      |* Press '1' to search; 'Users', 'Tickets' or 'Organizations'
      |* Press '2' to view all searchable fields
      |* Type 'quit' to exit
      |""".stripMargin

  val searchMessage: String =
    """
      |* Press '1' to search 'Users'
      |* Press '2' to search 'Tickets'
      |* Press '3' to search 'Organizations'
      |""".stripMargin
}
