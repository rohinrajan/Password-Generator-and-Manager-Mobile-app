<?php
define('HOST','mysql.hostinger.in');
define	('USER','u777023861_min1');
define ('PASS', 'android123');	
define ('DB','u777023861_test1');

class dbconnect{
	
        function connect(){
		$con = mysqli_connect(HOST,USER,PASS,DB);
		return $con;
	}
}
?>	