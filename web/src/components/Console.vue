<template>
  <div id="console">
    <Chat :messages="messages" />
    <form @submit.prevent="execute">
      <input
        type="text"
        placeholder="enter command.."
        v-model="value"
        @input="check"
        @keydown="cycle"
      />
    </form>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import _ from "lodash";
import Chat, { Message } from "./Chat.vue";
import { EventHub } from "../App.vue";

@Component({
  components: { Chat }
})
export default class Console extends Vue {
  value = "";
  history: string[] = ["First", "Second", "Last"];
  position = 0;

  messages: Message[] = new Array(100)
    .fill([
      {
        timestamp: new Date().getTime(),
        text: "Test"
      },
      {
        timestamp: new Date().getTime() - 200,
        text: "Test 2"
      }
    ])
    .flat();

  private check = _.debounce((e: any) => {
    const input = e.target?.value;
    // Send to brigardier
  }, 500);

  private cycle(event: KeyboardEvent) {
    const { keyCode } = event;

    switch (keyCode) {
      case 38:
        this.position++;
        break;

      case 40:
        this.position--;
        break;
    }

    if ([38, 40].includes(keyCode)) {
      this.position = Math.max(0, Math.min(this.history.length, this.position));
      this.value = this.history[this.history.length - this.position] ?? "";
      event.preventDefault();
    }
  }

  scrollDown() {
    EventHub.$emit("scroll");
  }

  execute() {
    this.history.push(this.value);
    this.messages.push({ text: this.value, timestamp: new Date().getTime() });
    this.value = "";
    this.position = 0;
    this.scrollDown();
  }
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
