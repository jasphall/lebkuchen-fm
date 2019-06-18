package xyz.deseteral.lebkuchenfm.api.commands.text

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import xyz.deseteral.lebkuchenfm.IntegrationSpecification
import xyz.deseteral.lebkuchenfm.api.commands.text.TextCommandResponseDto

import static groovy.json.JsonOutput.toJson

class TextCommandControllerIntegrationTest extends IntegrationSpecification {
    def 'should respond to ping command'() {
        given:
        def body = [text: '/fm ping']
        def request = RequestEntity.post(localUri('/commands/text'))
            .contentType(MediaType.APPLICATION_JSON)
            .body(toJson(body))

        when:
        def response = restTemplate.exchange(request, TextCommandResponseDto)

        then:
        response.statusCode == HttpStatus.OK
        response.body.response == 'pong'
    }

    def 'should respond to not existing command'() {
        given:
        def body = [text: '/fm notExisting']
        def request = RequestEntity.post(localUri('/commands/text'))
            .contentType(MediaType.APPLICATION_JSON)
            .body(toJson(body))

        when:
        def response = restTemplate.exchange(request, TextCommandResponseDto)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
        response.body.response == "Command 'notExisting' does not exist"
    }

    def 'should respond to text that is not a command'() {
        given:
        def body = [text: 'some test string']
        def request = RequestEntity.post(localUri('/commands/text'))
            .contentType(MediaType.APPLICATION_JSON)
            .body(toJson(body))

        when:
        def response = restTemplate.exchange(request, TextCommandResponseDto)

        then:
        response.statusCode == HttpStatus.UNPROCESSABLE_ENTITY
        response.body.response == "Text 'some test string' is not a command"
    }
}