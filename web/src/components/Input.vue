<template>
  <form @submit.prevent="execute">
    <input
      type="text"
      placeholder="enter command.."
      v-model="value"
      @input="check"
      @keydown="cycle"
    />
  </form>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import _ from "lodash";
import { EventHub } from "../App.vue";

@Component
export default class Input extends Vue {
  value = "";
  history: string[] = [];
  position = 0;

  private check = _.debounce((e: { target?: HTMLInputElement }) => {
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
    //this.messages.push({ text: this.value, timestamp: new Date().getTime() });
    this.value = "";
    this.position = 0;
    this.scrollDown();
  }
}
</script>

<style scoped>
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
