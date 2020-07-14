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
import { EventHub } from "../App.vue";

@Component({
  components: { Chat, Input }
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

input {
  width: calc(100% - 40px);
  background: transparent;
  color: #eee;
  border: none;
  font-size: 1rem;
  padding: 10px 20px;
  box-shadow: 0 -10px 10px 0 #0002;
}

input::placeholder {
  color: #eee8;
}

input:focus {
  outline: none;
}
</style>
