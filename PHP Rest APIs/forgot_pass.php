<?php
 require_once('DB_Connect.php');
 $dbobj = new dbconnect();
 $con = $dbobj->connect();

 if($con == mysqli_connect_error())
	 echo "Error in connecting with the database";
 else{

 
	$user_id = $_POST['user_id'];
	$website = $_POST['website'];
 
	//Get the password id then get the passphrase
	$query = "select tempAID from userpasswords where UID = $user_id and website = '$website'";
	$result = mysqli_query($con,$query);
	$pwds = array();
	while($data = mysqli_fetch_assoc($result)){
		$tempAID = $data['tempAID'];
	}
	
	$query = "select TemplateAnswer from template_answers where TempAID = $tempAID";
	if($result = mysqli_query($con,$query)) {
		while($rowvalue = mysqli_fetch_assoc($result)){
		    $passPhrase = $rowvalue['TemplateAnswer'];
        }
	}

	$sql_email = "select  name,UserName,Email_Address from usercredentials where userID=$user_id";
	$results_sql_email = mysqli_query($con,$sql_email);

	while($row_sql_email = mysqli_fetch_assoc($results_sql_email))
	{
		$retr_email = $row_sql_email['Email_Address'];
		$retr_UserName = $row_sql_email['UserName'];
		$retr_Name = $row_sql_email['name'];
	}
	
	$message="Name: $retr_Name \n UserName: $retr_UserName \n Website: $website \n PassPhrase: $passPhrase";


    if(mail($retr_email, 'Your Pass-phrase', $message))
	{
	echo "A email has been sent";
	}
 }	
 ?>		