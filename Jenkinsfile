node {

   stage('Preparation') {
      git 'https://github.com/evandor/skysail-um.git'
   }
   
   stage('build') {
      //buildCode()
      sh './gradlew clean build'
   }

   stage('cucumber') {
	 //build 'skysail.cucumber'
	 //step([$class: 'CucumberReportPublisher', failedFeaturesNumber: 0, failedScenariosNumber: 0, failedStepsNumber: 0, fileExcludePattern: '', fileIncludePattern: '**/cucumber.json', jsonReportDirectory: '', parallelTesting: false, pendingStepsNumber: 0, skippedStepsNumber: 0, trendsLimit: 0, undefinedStepsNumber: 0])
   }   
   
   stage('publishHTML') {
     publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: '', reportFiles: 'index.html', reportName: 'HTML Report'])
   }
   
   /*stage('deployment.int') {
      parallel (
  	    demo:            { build 'ssp.demo.export.int' }
	    //pact_int:        { build 'ssp.pact.export.int' },
	    //pact_standalone: { build 'ssp.pact.export.standalone' }
	  )
   }*/

   /*stage('stresstest') {
     sh './gradlew skysail.product.demo.e2e.gatling:gatlingRun -DbaseUrl=http://192.168.100.3:8391/'
     gatlingArchive()
   }*/

   /*stage('document') {
      parallel (
	    //code:    { buildCode() },
		//doc:     { build 'skysail.doc' },
   	    javadoc: { buildJavadoc() }
	  )
   }*/   
   
}

def buildCode() {
  sh './gradlew build'
}

def buildJavadoc() {
  sh './gradlew javadoc'
  publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'skysail.app.notes/generated/docs/javadoc', reportFiles: 'index.html', reportName: 'Javadoc'])
}