<template>
  <form @submit.prevent="execute">
    <input autocomplete="false" type="text" v-model="value" @input="requestCheck" @keydown="cycle" />
    <Suggestions ref="suggestions" :value="value" @complete="complete" />
  </form>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { clamp, debounce } from "lodash";
import { EventHub } from "../main";
import Connection from "../Connection";
import Suggestions, { Suggestion } from "./Suggestions.vue";

@Component({
  components: { Suggestions }
})
export default class Input extends Vue {
  private value = "";
  private history: string[] = [];
  private position = 0;

  private requestCheck = debounce((e: { target?: HTMLInputElement }) => {
    const input = e.target?.value;
    if (input) Connection.checkCommand(input);
  }, 500);

  private cycle(event: KeyboardEvent) {
    const { keyCode } = event;
    const keys = [38, 40];

    const index = keys.indexOf(keyCode);

    if (index >= 0) {
      const by = index * 2 - 1;
      this.position = clamp(this.position + by, 0, this.history.length);
      this.value = this.history[this.history.length - this.position] ?? "";
      event.preventDefault();
    }

    if (keyCode === 9) {
      event.preventDefault();
      const suggestions = this.$refs.suggestions as Suggestions;
      suggestions?.tab();
    }
  }

  scrollDown() {
    EventHub.$emit("scroll");
  }

  complete(suggestion: Suggestion) {
    this.value = this.value.substring(0, suggestion.start) + suggestion.text;
  }

  execute() {
    Connection.checkCommand(this.value);
    this.history.push(this.value);
    //this.messages.push({ text: this.value, timestamp: new Date().getTime() });
    this.value = "";
    this.position = 0;
    this.scrollDown();
  }
}
</script>

<style scoped lang="scss">
form {
  align-self: flex-end;
  position: relative;

  & > * {
    padding: 10px 20px;
  }

  input {
    width: calc(100% - 40px);
    background: transparent;
    color: #eee;
    border: none;
    font-size: 1rem;
    box-shadow: 0 -10px 10px 0 #0002;

    &::placeholder {
      color: #eee8;
    }

    &:focus {
      outline: none;
    }
  }
}
</style>
