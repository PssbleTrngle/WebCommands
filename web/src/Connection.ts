import { EventHub } from './main'

interface Message {
    type: string;
    data?: string;
}

class Connection {

    private socket!: WebSocket

    constructor(port: number) {
        this.socket = new WebSocket(`ws://somethingcatchy.net:${port}`)
        this.socket.onopen = () => console.log('Connection opened')
        this.socket.onmessage = m => this.handleMessage(JSON.parse(m.data))
    }

    checkCommand(command: string) {
        if(this.socket.OPEN) this.socket.send(JSON.stringify({ type: 'check', command }))
    }

    handleMessage(message: Message) {
        const { type, data } = message;
        const parsed = data && JSON.parse(data)

        if(['message', 'check'].includes(type)) EventHub.$emit(type, parsed);
        else console.log(`Received message of type '${type}'`)
    }

}

export default new Connection(7011)