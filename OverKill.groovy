@Grab(group='com.morpheusdata', module='morpheus-plugin-api', version='0.15.3')
@Grab(group='io.reactivex.rxjava2', module='rxjava', version='2.2.0')
@Grab(group='org.spockframework', module='spock-core', version='2.2-groovy-2.5', scope='test')
@Grab(group='org.codehaus.groovy', module='groovy-xml', version='3.0.20')
@Grab(group='org.codehaus.groovy', module='groovy-json', version='2.5.14')

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.MorpheusUtils
import com.morpheusdata.core.OptionSourceProvider
import com.morpheusdata.core.Plugin
import spock.lang.Specification
import spock.lang.Subject
import groovy.json.*
import groovy.json.JsonOutput

def ospMock = new OptionSourceProviderMock()

class OptionSourceProviderMock extends Specification {
    @Subject 
    PersonalDetailsOptionSourceProvider service 
    
    MorpheusContext                     context
    PersonalDetailsPlugin 				plugin
    
    void setup() {
		context = Mock(MorpheusContext)
		plugin = Mock(PersonalDetailsPlugin)

		service = new PersonalDetailsOptionSourceProvider(plugin, context)
	}
    
    void "getMethodNames"() {
        when:
            def methodNames = service.getMethodNames()
            def name = "Uthman"
            def personalDetails = service.getPersonalDetails(name)
        then:
			methodNames           
    }
    
    void "getPersonalDetails"() {
        
        when:
            def name = "Uthman"
            def personalDetails = service.getPersonalDetails(name)
            
        then:
            def results = personalDetails.grep()
            println """

                                        [ TEST ]
                -----------------------------------------------------------
                  name:   ${name}
                  data:   ${results}

                  type:   ${results.class}


                """                
    }
    
    void "getDebugInfo"() {
        
        when:
            def name = "Uthman"
            def getPersonalDetails = service.getPersonalDetails(name)
            def methodNames = service.getMethodNames()
        
        then:
            println """
                                        [ DEBUG ]
                  -----------------------------------------------------------
 
	 		  class:	${service.class}

			   name:    ${service.name}
               code:	${service.code}
                
        methodNames:	${service.methodNames} - ${methodNames.class}
    morpheusContext:	${service.morpheusContext}
    	   morpheus:	${service.morpheus}
           
              props:    ${service.properties}

"""

    }
}

class PersonalDetailsOptionSourceProvider implements OptionSourceProvider{

	Plugin plugin
	MorpheusContext morpheusContext

	PersonalDetailsOptionSourceProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'personal-details-option-source-provider'
	}

	@Override
	String getName() {
		return 'Personal Details Option Source Provider'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['personalDetails'])
	}

	def getPersonalDetails(String name) {
		switch(name) {
			case "Uthman":					// name, location, organization
				return [ [name: 'name', value: 'uthman'], [name: 'location', value: 'london'], [name: 'organization', value: 'morpheus-data-org'] ]
				break

			case "Test User":
				return [ [name: 'name', value: 'test-user'], [name: 'location', value: 'silicon-valley'], [name: 'organization', value: 'apple-org'] ]
				break
				
			default: 
				return [ [name: "name", value: null], [name: "location", value: null], [name: "organization", value: null] ]
				break
		}	
	}
}

class PersonalDetailsPlugin extends Plugin {

	@Override
	String getCode() {
		return 'personal-details-plugin'
	}

	@Override
	void initialize() {
		this.setName("Personal Details Example - AbstractDatasetProvider and OptionSourceProvider")
	//	this.registerProvider(new PersonalDetailsDatasetProvider(this, this.morpheus))
		this.registerProvider(new PersonalDetailsOptionSourceProvider(this, this.morpheus))

/* 		this.settings << new OptionType (
			name: 'DatasetProvider Options',
			code: 'dataset-provider-options',
			fieldName: 'pluginApiDataset',
			displayOrder: 0,
			fieldLabel: 'Select a network',
			helpText: '',
			required: false,
			optionSource: 'pluginApiDatasetExample',
			optionSourceType: 'example',
			inputType: OptionType.InputType.SELECT
		) */
	}

	@Override
	void onDestroy() {
	}
}
