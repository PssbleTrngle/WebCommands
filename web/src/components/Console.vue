<template>
  <div id="console">
    <Chat :messages="messages" />
    <Input />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import _ from "lodash";
import Chat, { Message } from "./Chat.vue";
import Input from "./Input.vue";
import { EventHub } from "../main";

@Component({
  components: { Chat, Input },
  created: function (this: Console) {
    EventHub.$on('message', (msg: Message) => {
      this.messages.push({ ...msg, timestamp: new Date().getTime() })
    })
  }
})
export default class Console extends Vue {
  
  messages: Message[] = []

}
</script>

<style scoped>
#console {
  color: #eee;
  background: #666b7f;

  height: 800px;
  max-height: calc(100vh - 40px);
  width: 1400px;
  max-width: 100vw;

  display: grid;
  grid-template: "chat" "input" / 1fr;
  overflow: hidden;
}

form {
  align-self: flex-end;
}
</style>
