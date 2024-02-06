import mitt from 'mitt'
const emitter = mitt()

console.log('import globalEventBus')
export default emitter;