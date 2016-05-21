<?
require_once('DB_Connect.php');
 $dbobj = new dbconnect();
 $con = $dbobj->connect();
 
 if($con == mysqli_connect_error())
	 echo "Error in connecting with the database";
 else{

	 $userid = $_POST['userid'];
	 $website = $_POST['website'];
	 $passphrase = $_POST['passphrase'];
	 
	 //get the tempAID 
	 // get the PwdID
	 $query1 = "select PwdID,tempAID from userpasswords where UID= $userid and website = '$website'";
	 $result1 = mysqli_query($con,$query1);
	 
	 while($row_value = mysqli_fetch_assoc($result1))
	 {
			$pwdID = $row_value['PwdID'];
			$tempsID = $row_value['tempAID'];
	 }
	

	 // get the phrase id from the based on the information given by the user
	 $query3 = "select TempAID from template_answers where TemplateAnswer = '$passphrase'";
	 if($result3 = mysqli_query($con,$query3)){
		 while($row3 = mysqli_fetch_assoc($result3)){
			 $cmptempAID = $row3['TempAID'];
		 }
	 }

	 if($cmptempAID == $tempsID){
		$query2 = "select encryPasswd from masterpasswords where PwdID = '$pwdID'";
		$result2 = mysqli_query($con,$query2);
		{
			while($row1 = mysqli_fetch_assoc($result2))
			{
				$encodedpassword = $row1['encryPasswd'];
				$password = base64_decode($encodedpassword);
				echo $password;
			}
	 	} 
	}
	else{
		echo "Unable to find PassPhrase";
	}
 }
?>