/**
 * @author :    sean
 * @mailto :    seanoc5@gmail.com
 * @created :   6/27/22, Monday
 * @description:
 */



import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.log4j.Logger

final Logger log = Logger.getLogger(this.class.name);
log.info "Starting ${this.class.name}..."
def jsonSlurper = new JsonSlurper()
boolean pretty = true
def src = getClass().getResourceAsStream('/query-pipelines/system-logs-browser.objects.json')
def slurpedObject = jsonSlurper.parse(src)
def query = slurpedObject.objects.queryPipelines[0]
log.info "Source json: ${src}: ${query.id}"

//String queryLabel = 'slb'
File stagesDir = new File(getClass().getResource('/query-pipelines/stages').toURI())

def stages = query.stages
stages.each { Map stage ->
    String id = stage.id
    String type = stage.type
    String label = stage.label
    if(!label){
        label = "${type}.${id}"
        log.info "No label found, using Type and id: $label"
    }
    String name = label.replaceAll(' ', '_')
    File outFile = new File(stagesDir, "${name}.json")
    log.info "Export stage ($id) type: ${stage.type}, name: ${name} to file: ${outFile}"
    String jsonString =  JsonOutput.toJson(stage)
    if(pretty){
        outFile.text = JsonOutput.prettyPrint(jsonString)
    } else {
        outFile.text = jsonString
    }
}


log.info "Done...?"
